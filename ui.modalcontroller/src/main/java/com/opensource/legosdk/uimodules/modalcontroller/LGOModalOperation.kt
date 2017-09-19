package com.opensource.legosdk.uimodules.modalcontroller

import android.content.Intent
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import com.opensource.legosdk.core.LGOModalWebViewActivity
import java.net.URI
import java.net.URL

/**
 * Created by cuiminghui on 2017/5/15.
 */
class LGOModalOperation(val request: LGOModalRequest): LGORequestable() {

    fun requestURL(url: String): URL? {
        var relativeURL = url
        val webView = request.context?.sender as? WebView ?: return null
        if (!relativeURL.startsWith("http://") && !relativeURL.startsWith("https://") && !relativeURL.startsWith("content://")) {
            val uri = URI(webView.url)
            relativeURL = uri.resolve(relativeURL).toString()
        }
        return URL(relativeURL)
    }

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestContentContext()?.let {
            when (request.opt) {
                "present" -> {
                    request.context?.runOnMainThread {
                        val intent = Intent(it, LGOModalWebViewActivity::class.java)
                        request.path?.let {
                            intent.putExtra("LGOModalController.RequestPath", requestURL(it))
                        }
                        intent.putExtra("LGOModalController.Class", true)
                        request.args?.let {
                            intent.putExtra("LGOModalController.args", it.toString())
                        }
                        intent.putExtra("LGOModalController.ModalType", request.modalStyle.type.ordinal)
                        intent.putExtra("LGOModalController.ModalWidth", request.modalStyle.modalWidth)
                        intent.putExtra("LGOModalController.ModalHeight", request.modalStyle.modalHeight)
                        intent.putExtra("LGOModalController.clearMask", request.clearMask)
                        intent.putExtra("LGOModalController.nonMask", request.nonMask)
                        request.preloadToken?.let {
                            intent.putExtra("LGOModalController.preloadToken", it)
                        }
                        it.startActivity(intent)
                    }
                    return LGOResponse().accept(null)
                }
                "dismiss" -> {
                    request.context.requestActivity()?.finish()
                }
                else -> {
                    return LGOResponse().reject("UI.LGOModalController", -2, "invalid opt.")
                }
            }
        }
        return LGOResponse().accept(null)
    }

}