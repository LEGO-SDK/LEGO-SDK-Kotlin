package com.opensource.legosdk.uimodules.navigationcontroller

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/24.
 */
class LGONavigationRequest(context: LGORequestContext?) : LGORequest(context) {

    var opt: String = "push"

    var path: String? = null

    var animated: Boolean = true

    var args: JSONObject? = null

    var preloadToken: String? = null

}