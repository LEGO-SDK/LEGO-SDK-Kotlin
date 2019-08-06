package com.opensource.legosdk.uimodules.navigationcontroller

import android.content.Intent
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import com.opensource.legosdk.core.LGOWebViewActivity
import java.net.URI

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
                            it.optString("customID")?.let {
                                intent.putExtra("LGONavigationController.customID", it.toString())
                            }
                        }
                        request.preloadToken?.let {
                            intent.putExtra("LGONavigationController.preloadToken", it)
                        }
                        request.context.requestChildViewControllers().add(it)
                        it.startActivity(intent)
                    }
                    return LGOResponse().accept(null)
                }
                "pop" -> {
                    request.context.requestActivity()?.finish()
                    request.args?.get("customID")?.let {
                        val customID = it.toString()
                        var childs = request.context.requestChildViewControllers()
                        var resChilds = childs.reversed()
                        val listIterator = resChilds.listIterator()
                        while (listIterator.hasNext()) {
                            val activity = listIterator.next()
                            val intent = activity.intent
                            val cID = intent.getStringExtra("LGONavigationController.customID")
                            if (cID == null || cID.equals(customID)) {
                                childs.clear()
                                break
                            } else {
                                childs.remove(activity)
                                activity.finish()
                            }
                        }
                    }
                }
                else -> {
                    return LGOResponse().reject("UI.NavigationController", -2, "invalid opt.")
                }
            }
        }
        return LGOResponse().reject("UI.NavigationController", -1, "Context error.")
    }

}