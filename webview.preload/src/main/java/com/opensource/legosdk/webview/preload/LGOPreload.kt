package com.opensource.legosdk.webview.preload

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/8/10.
 */
class LGOPreload: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        if (pool.size >= 3) {
            return LGORequestable.reject("WebView.Preload", -2, "Too much preload webview. max limit 3.")
        }
        val token = obj.optString("token") ?: return LGORequestable.reject("WebView.Preload", -1, "token required.")
        val url = obj.optString("url") ?: return LGORequestable.reject("WebView.Preload", -1, "url required.")
        return LGOPreloadOperation(LGOPreloadRequest(token, url, context))
    }

    fun fetchWebView(token: String): LGOWebView? {
        pool[token]?.let { view ->
            LGOWebView(view.context)?.let {
                view.url?.let { url ->
                    it.loadUrl(url)
                }
                pool.put(token, it)
            }
            return view
        }
        return null
    }

    companion object {

        val pool: HashMap<String, LGOWebView> = hashMapOf()

        init {
            LGOCore.modules.addModule("WebView.Preload", LGOPreload())
        }

    }

}

class LGOPreloadRequest(val token: String, val url: String, context: LGORequestContext?) : LGORequest(context)

class LGOPreloadOperation(val request: LGOPreloadRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestActivity()?.let { context ->
            context.runOnUiThread {
                LGOWebView(context)?.let {
                    it.loadUrl(request.url)
                    LGOPreload.pool[request.token] = it
                }
            }
        }
        return LGOResponse().accept(null)
    }

}