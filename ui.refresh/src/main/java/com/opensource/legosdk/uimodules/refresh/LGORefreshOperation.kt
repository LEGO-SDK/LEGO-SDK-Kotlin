package com.opensource.legosdk.uimodules.refresh

import android.graphics.Color
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
        request.context?.runOnMainThread {
            when (request.opt) {
                "create" -> {
                    (request.context?.sender as? WebView)?.let {
                        (it.parent as? SwipeRefreshLayout)?.let {
                            it.isEnabled = true
                            it.setOnRefreshListener { callbackBlock(LGOResponse().accept(null)) }
                        }
                    }
                }
                "complete" -> {
                    (request.context?.sender as? WebView)?.let {
                        (it.parent as? SwipeRefreshLayout)?.let {
                            it.isRefreshing = false
                        }
                    }
                }
                else -> { }
            }
        }
    }

}