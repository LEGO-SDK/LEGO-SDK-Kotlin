package com.opensource.legosdk.nativemodules.filemanager

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by PonyCui_Home on 2017/4/19.
 */
class LGOFileRequest(context: LGORequestContext?) : LGORequest(context) {

    var suite: String? = null
    var opt: String? = null
    var filePath: String? = null
    var fileContents: String? = null

}