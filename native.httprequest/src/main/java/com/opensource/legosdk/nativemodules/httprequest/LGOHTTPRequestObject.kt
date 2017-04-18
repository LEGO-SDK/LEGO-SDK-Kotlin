package com.opensource.legosdk.nativemodules.httprequest

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGOHTTPRequestObject(context: LGORequestContext?): LGORequest(context) {

    var URL: String? = null

    var timeout = 15

    var headers: JSONObject? = null

    var data: String? = null

}