package com.opensource.legosdk.uimodules.modalcontroller

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/5/15.
 */
class LGOModalController: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGOModalRequest(context)
        request.opt = obj.optString("opt", "present")
        request.path = obj.optString("path") ?: return LGORequestable.reject("UI.ModalController", -1, "params 'path' required!")
        request.args = obj.optJSONObject("args")
        request.animated = obj.optBoolean("animated", true)
        request.clearWebView = obj.optBoolean("clearWebView", false)
        request.clearMask = obj.optBoolean("clearMask", false)
        request.nonMask = obj.optBoolean("nonMask", false)
        when (obj.optInt("modalType", 0)) {
            1 -> request.modalStyle.type = LGOModalRequest.ModalType.Center
            2 -> request.modalStyle.type = LGOModalRequest.ModalType.Top
            3 -> request.modalStyle.type = LGOModalRequest.ModalType.Left
            4 -> request.modalStyle.type = LGOModalRequest.ModalType.Bottom
            5 -> request.modalStyle.type = LGOModalRequest.ModalType.Right
        }
        request.modalStyle.modalWidth = obj.optInt("modalWidth", -1)
        request.modalStyle.modalHeight = obj.optInt("modalHeight", -1)
        request.preloadToken = obj.optString("preloadToken")
        return LGOModalOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOModalRequest ?: return null
        return LGOModalOperation(request)
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.ModalController", LGOModalController())
        }

    }

}