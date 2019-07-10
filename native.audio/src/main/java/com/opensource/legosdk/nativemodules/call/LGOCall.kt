package com.opensource.legosdk.nativemodules.audio

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by Errnull on 2019/7/9.
 */
class LGOAudio: LGOModule() {
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
        return LGOAudioOperation(LGOAudioRequest(
                obj.optString(("methodName")),
                userInfoMap,
                context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOAudioRequest ?: return null
        return LGOAudioOperation(request)
    }
}