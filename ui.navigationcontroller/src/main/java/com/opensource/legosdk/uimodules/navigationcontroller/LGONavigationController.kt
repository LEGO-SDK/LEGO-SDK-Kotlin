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
        return LGONavigationOperation(request)
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.NavigationController", LGONavigationController())
        }

    }

}