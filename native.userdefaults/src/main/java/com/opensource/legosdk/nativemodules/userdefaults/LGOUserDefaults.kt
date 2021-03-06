package com.opensource.legosdk.nativemodules.userdefaults

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOUserDefaults: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGOUserDefaultsRequest(context)
        request.opt = obj.optString("opt", "read")
        request.suite = obj.optString("suite", "default")
        request.key = obj.optString("key")
        obj.optJSONObject("value")?.let {
            request.value = "Object>>>" + it.toString()
        }
        obj.optString("value")?.let {
            request.value = it
        }
        return LGOUserDefaultsOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        (request as? LGOUserDefaultsRequest)?.let {
            return LGOUserDefaultsOperation(it)
        }
        return null
    }

}