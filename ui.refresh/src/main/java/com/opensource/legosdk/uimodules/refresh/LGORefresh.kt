package com.opensource.legosdk.uimodules.refresh

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGORefresh: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGORefreshOperation(LGORefreshRequest(obj.optString("opt", ""), context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGORefreshRequest ?: return null
        return LGORefreshOperation(request)
    }

}