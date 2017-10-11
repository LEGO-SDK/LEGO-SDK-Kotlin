package com.opensource.legosdk.nativemodules.notification

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGONotification: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGONotificationRequest(context)
        request.opt = obj.optString("opt", "create")
        request.name = obj.optString("name")
        request.aPostObject = obj.optString("aPostObject")
        request.aPostUserInfo = obj.optJSONObject("aPostUserInfo")
        return LGONotificationOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        (request as? LGONotificationRequest)?.let {
            return LGONotificationOperation(it)
        }
        return null
    }

}