package com.opensource.legosdk.uimodules.navigationcontroller

import android.content.Intent
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import com.opensource.legosdk.core.LGOWebViewActivity
import java.net.URI
import java.net.URL

/**
 * Created by cuiminghui on 2017/4/24.
 */
class LGONavigationOperation(val request: LGONavigationRequest): LGORequestable() {

    private fun requestURL(url: String): String {
        var relativeURL = url
        val webView = request.context?.sender as? WebView ?: return relativeURL
        if (!relativeURL.startsWith("http://") && !relativeURL.startsWith("https://") && !relativeURL.startsWith("content://")) {
            val uri = URI(webView.url)
            relativeURL = uri.resolve(relativeURL).toString()
        }
        return relativeURL
    }

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestActivity()?.let {
            when (request.opt) {
                "push" -> {
                    request.context?.runOnMainThread {
                        val intent = Intent(it, LGOWebViewActivity::class.java)
                        request.path?.let {
                            intent.putExtra("LGONavigationController.RequestPath", requestURL(it))
                        }
                        intent.putExtra("LGONavigationController.Class", true)
                        request.args?.let {
                            intent.putExtra("LGONavigationController.args", it.toString())
                        }
                        request.preloadToken?.let {
                            intent.putExtra("LGONavigationController.preloadToken", it)
                        }
                        it.startActivity(intent)
                    }
                    return LGOResponse().accept(null)
                }
                "pop" -> {
                    request.context.requestActivity()?.finish()
                }
                else -> {
                    return LGOResponse().reject("UI.NavigationController", -2, "invalid opt.")
                }
            }
        }
        return LGOResponse().reject("UI.NavigationController", -1, "Context error.")
    }

}