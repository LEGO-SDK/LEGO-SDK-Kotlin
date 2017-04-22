package com.opensource.legosdk.uimodules.alertview

import android.app.AlertDialog
import android.content.DialogInterface
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOAlertViewOperation(val request: LGOAlertViewRequest): LGORequestable() {

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        request.context?.requestActivity()?.let {
            it.runOnUiThread {
                val builder = AlertDialog.Builder(it)
                builder.setTitle(request.title)
                builder.setMessage(request.message)
                if (0 < request.buttonTitles.size) {
                    builder.setNegativeButton(request.buttonTitles[0], { _, idx ->
                        callbackBlock(LGOAlertViewResponse(0).accept(null))
                    })
                }
                if (1 < request.buttonTitles.size) {
                    builder.setPositiveButton(request.buttonTitles[1], { _, idx ->
                        callbackBlock(LGOAlertViewResponse(1).accept(null))
                    })
                }
                builder.create().show()
            }
        }
    }

}