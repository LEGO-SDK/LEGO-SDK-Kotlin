package com.opensource.legosdk.uimodules.actionsheet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.TypedArray
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOActionSheetOperation(val request: LGOActionSheetRequest): LGORequestable() {

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        request.context?.requestActivity()?.let {
            it.runOnUiThread {
                val builder = AlertDialog.Builder(it)
                builder.setTitle(request.title)
                builder.setItems(request.buttonTitles.toTypedArray(), { _, idx ->
                    callbackBlock(LGOActionSheetResponse(idx).accept(null))
                })
                builder.create().show()
            }
        }
    }

}