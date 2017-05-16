package com.opensource.legosdk.uimodules.modalcontroller

import android.content.Intent
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import com.opensource.legosdk.core.LGOModalWebViewActivity

/**
 * Created by cuiminghui on 2017/5/15.
 */
class LGOModalOperation(val request: LGOModalRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestContentContext()?.let {
            when (request.opt) {
                "present" -> {
                    val intent = Intent(it, LGOModalWebViewActivity::class.java)
                    intent.putExtra("LGOModalController.RequestPath", request.path)
                    intent.putExtra("LGOModalController.Class", true)
                    request.args?.let {
                        intent.putExtra("LGOModalController.args", it.toString())
                    }
                    intent.putExtra("LGOModalController.ModalType", request.modalStyle.type.ordinal)
                    intent.putExtra("LGOModalController.ModalWidth", request.modalStyle.modalWidth)
                    intent.putExtra("LGOModalController.ModalHeight", request.modalStyle.modalHeight)
                    intent.putExtra("LGOModalController.clearMask", request.clearMask)
                    intent.putExtra("LGOModalController.nonMask", request.nonMask)
                    it.startActivity(intent)
                    return LGOResponse().accept(null)
                }
                "dismiss" -> {
                    request.context.requestActivity()?.finish()
                }
                else -> {
                    return LGOResponse().reject("UI.LGOModalController", -2, "invalid opt.")
                }
            }
        }
        return LGOResponse().accept(null)
    }

}