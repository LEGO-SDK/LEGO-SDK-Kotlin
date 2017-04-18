package com.opensource.legosdk.nativemodules.pasteboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse


/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOPasteboardOperation(val request: LGOPasteboardRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        when (request.opt) {
            "update" -> {
                (request.context?.requestContentContext()?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.let {
                    it.primaryClip = ClipData.newPlainText("default", request.string)
                    return LGOResponse().accept(null)
                }
            }
            "read" -> {
                (request.context?.requestContentContext()?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.let {
                    val item = it.primaryClip.getItemAt(0)
                    item?.let {
                        val response = LGOPasteboardResponse(it.text.toString().trim { it <= ' ' })
                        return response.accept(null)
                    }
                    return LGOResponse().reject("Native.Pasteboard", -2, "null value")
                }
            }
            "delete" -> {
                (request.context?.requestContentContext()?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.let {
                    it.primaryClip = ClipData.newPlainText("default", "")
                    return LGOResponse().accept(null)
                }
            }

        }
        return LGOResponse().reject("Native.Pasteboard", -1, "invalid opt value.")
    }

}