package com.opensource.legosdk.uimodules.modalcontroller

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/5/15.
 */
class LGOModalRequest(context: LGORequestContext?) : LGORequest(context) {

    enum class ModalType {
        Normal,
        Center,
        Top,
        Left,
        Bottom,
        Right,
    }

    inner class LGOModalStyle(var type: ModalType = ModalType.Normal, var modalWidth: Int = -1, var modalHeight: Int = -1)

    var opt: String = "present"
    var path: String? = null
    var animated: Boolean = true
    var clearWebView: Boolean = false
    var clearMask: Boolean = false
    var nonMask: Boolean = false
    var args: JSONObject? = null
    var modalStyle: LGOModalStyle = LGOModalStyle()

}