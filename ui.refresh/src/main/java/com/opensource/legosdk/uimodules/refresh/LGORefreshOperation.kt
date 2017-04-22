package com.opensource.legosdk.uimodules.refresh

import android.support.v4.widget.SwipeRefreshLayout
import android.view.ViewGroup
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGORefreshOperation(val request: LGORefreshRequest): LGORequestable() {

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        request.context?.requestActivity()?.let {
            it.runOnUiThread {
                when (request.opt) {
                    "create" -> {
                        (request.context?.sender as? WebView)?.let {
                            val webViewParent = it.parent
                            if (webViewParent is SwipeRefreshLayout) {
                                webViewParent.isEnabled = true
                                webViewParent.setOnRefreshListener { callbackBlock(LGOResponse().accept(null)) }
                                return@runOnUiThread
                            }
                            (webViewParent as ViewGroup).removeView(it)
                            val swipeRefreshLayout = SwipeRefreshLayout(it.context)
                            swipeRefreshLayout.setOnRefreshListener { callbackBlock(LGOResponse().accept(null)) }
                            swipeRefreshLayout.addView(it, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                            webViewParent.addView(swipeRefreshLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                        }
                    }
                    "complete" -> {
                        (request.context?.sender as? WebView)?.let {
                            val webViewParent = it.parent
                            if (webViewParent is SwipeRefreshLayout) webViewParent.isRefreshing = false
                        }
                    }

                }
            }
        }
    }

}