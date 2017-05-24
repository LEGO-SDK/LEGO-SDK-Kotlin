package com.opensource.legosdk.nativemodules.notification

import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGONotificationResponse(val obj: String?, val userInfo: JSONObject?): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(
                Pair("object", obj ?: ""),
                Pair("userInfo", userInfo ?: JSONObject())
        )
    }

}