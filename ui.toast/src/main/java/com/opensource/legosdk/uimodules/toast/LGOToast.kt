package com.opensource.legosdk.uimodules.toast

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/6/12.
 */
class LGOToast: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val opt = obj.optString("opt") ?: "show"
        val style = obj.optString("style") ?: return LGORequestable.reject("UI.Toast", -1, "style required.")
        val title = obj.optString("title") ?: return LGORequestable.reject("UI.Toast", -1, "title required.")
        val timeout = Math.min(10, obj.optInt("timeout", 10))
        var masked = true
        if (obj.has("masked")) {
            masked = obj.optBoolean("masked")
        }
        return LGOToastOperation(LGOToastRequest(opt, style, title, timeout, masked, context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        (request as? LGOToastRequest)?.let {
            return LGOToastOperation(it)
        }
        return null
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.Toast", LGOToast())
        }

    }

}