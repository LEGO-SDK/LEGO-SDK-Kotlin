package com.opensource.legosdk.uimodules.navigationitem

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/5/11.
 */

class LGONavigationItem: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGONavigationItemOperation(LGONavigationItemRequest(obj.optString("leftItem"), obj.optString("rightItem"), context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGONavigationItemRequest ?: return null
        return LGONavigationItemOperation(request)
    }

}