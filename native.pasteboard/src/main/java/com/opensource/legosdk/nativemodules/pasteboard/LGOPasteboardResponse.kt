package com.opensource.legosdk.nativemodules.pasteboard

import com.opensource.legosdk.core.LGOResponse

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOPasteboardResponse(val string: String): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(
                Pair("string", string)
        )
    }

}