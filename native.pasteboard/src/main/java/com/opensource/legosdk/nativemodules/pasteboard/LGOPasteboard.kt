package com.opensource.legosdk.nativemodules.pasteboard

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOPasteboard: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOPasteboardOperation(LGOPasteboardRequest(opt = obj.optString("opt", "read"), string = obj.optString("string", ""), context = context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        (request as? LGOPasteboardRequest)?.let {
            return LGOPasteboardOperation(it)
        }
        return null
    }

    companion object {

        init {
            LGOCore.modules.addModule("Native.Pasteboard", LGOPasteboard())
        }

    }

}