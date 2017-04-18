package com.opensource.legosdk.nativemodules.httprequest

import android.util.Base64
import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGOHTTPResponseObject: LGOResponse() {

    var error = ""
    var responseText = ""
    var responseData: ByteArray? = null
    var statusCode = 0

    override fun resData(): HashMap<String, Any> {
        val responseObject: HashMap<String, Any> = hashMapOf()
        responseObject.put("error", error)
        responseObject.put("responseText", responseText)
        responseData?.let {
            responseObject.put("responseData", Base64.encodeToString(it, 0))
        }
        responseObject.put("statusCode", statusCode)
        return responseObject
    }

}