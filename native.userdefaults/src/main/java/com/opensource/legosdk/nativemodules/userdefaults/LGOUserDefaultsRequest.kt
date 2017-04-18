package com.opensource.legosdk.nativemodules.userdefaults

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOUserDefaultsRequest(context: LGORequestContext?) : LGORequest(context) {

    var suite = "default"
    var opt = "read"
    var key: String? = null
    var value = ""

}