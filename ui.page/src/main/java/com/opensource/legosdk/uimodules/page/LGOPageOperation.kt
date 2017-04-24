package com.opensource.legosdk.uimodules.page

import com.opensource.legosdk.core.*

/**
 * Created by cuiminghui on 2017/4/24.
 */
class LGOPageOperation(val request: LGOPageRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request.urlPattern?.let {
            val urlPattern = it
            (LGOCore.modules.moduleWithName("UI.Page") as? LGOPage)?.let {
                it.settings.put(urlPattern, request)
            }
        }
        if (request.urlPattern == null) {
            (request.context?.requestActivity() as? LGOWebViewActivity)?.pageSetting = request
        }
        (request.context?.requestActivity() as? LGOWebViewActivity)?.applyPageSetting()
        return LGOResponse().accept(null)
    }

}