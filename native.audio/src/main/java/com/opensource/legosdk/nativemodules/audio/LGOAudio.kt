package com.opensource.legosdk.nativemodules.audio

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by Errnull on 2019/7/9.
 */
class LGOAudio: LGOModule() {
    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOAudioOperation(LGOAudioRequest(
                obj.optString(("type")),
                context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOAudioRequest ?: return null
        return LGOAudioOperation(request)
    }
}