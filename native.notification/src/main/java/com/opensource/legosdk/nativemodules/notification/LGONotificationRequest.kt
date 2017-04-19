package com.opensource.legosdk.nativemodules.notification

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGONotificationRequest(context: LGORequestContext?) : LGORequest(context) {

    var opt: String? = null
    var name: String? = null
    var aPostObject: String? = null
    var aPostUserInfo: JSONObject? = null

}