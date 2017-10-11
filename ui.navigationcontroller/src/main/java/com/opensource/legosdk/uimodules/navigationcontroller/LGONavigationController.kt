package com.opensource.legosdk.uimodules.navigationcontroller

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/24.
 */
class LGONavigationController: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGONavigationRequest(context)
        request.opt = obj.optString("opt", "push")
        request.path = obj.optString("path")
        request.animated = obj.optBoolean("animated", true)
        request.args = obj.optJSONObject("args")
        request.preloadToken = obj.optString("preloadToken")
        return LGONavigationOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGONavigationRequest ?: return null
        return LGONavigationOperation(request)
    }

}