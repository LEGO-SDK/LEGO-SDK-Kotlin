package com.opensource.legosdk.uimodules.page

import com.opensource.legosdk.core.*

/**
 * Created by cuiminghui on 2017/4/24.
 */
class LGOPageOperation(val requests: List<LGOPageRequest>): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        requests?.forEach { request ->
            request.urlPattern?.let {
                val urlPattern = it
                (LGOCore.modules.moduleWithName("UI.Page") as? LGOPage)?.let {
                    val mutableMap = it.settings.toMutableMap()
                    mutableMap.put(urlPattern, request)
                    it.settings = mutableMap.toMap()
                }
            }
            if (request.urlPattern == null || request.urlPattern?.length == 0) {
                (request.context?.requestActivity() as? LGOWebViewActivity)?.usingCustomPageSetting = true
                (request.context?.requestActivity() as? LGOWebViewActivity)?.pageSetting = request
                request.context?.requestWebView()?.fragment?.usingCustomPageSetting = true
                request.context?.requestWebView()?.fragment?.pageSetting = request
            }
            (request.context?.requestActivity() as? LGOWebViewActivity)?.applyPageSetting()
            request.context?.requestWebView()?.fragment?.applyPageSetting()
        }
        return LGOResponse().accept(null)
    }

}