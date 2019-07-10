package com.opensource.legosdk.nativemodules.call

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by Errnull on 2019/7/9.
 */
class LGOCall: LGOModule() {
    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {

        val userInfoObject = obj.optJSONObject("userInfo")
        val userInfoMap = HashMap<String, String>()
        userInfoObject.keys()?.let {
            var key: String
            var value: String
            while (it.hasNext()) {
                key = it.next()
                value = userInfoObject.get(key).toString()
                userInfoMap.put(key, value)
            }
        }
        return LGOCallOperation(LGOCallRequest(
                obj.optString(("methodName")),
                userInfoMap,
                context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOCallRequest ?: return null
        return LGOCallOperation(request)
    }
}