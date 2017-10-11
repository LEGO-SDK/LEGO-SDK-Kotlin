package com.opensource.legosdk.nativemodules.httprequest

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGOHTTPRequest: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGOHTTPRequestObject(context)
        request.URL = obj.optString("URL")
        request.timeout = obj.optInt("timeout", 15)
        request.headers = obj.optJSONObject("headers")
        request.data = obj.optString("data")
        return LGOHTTPRequestOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        (request as? LGOHTTPRequestObject)?.let {
            return LGOHTTPRequestOperation(it)
        }
        return null
    }

}