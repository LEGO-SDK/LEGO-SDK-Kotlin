package com.opensource.legosdk.uimodules.actionsheet

import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOActionSheetResponse(val buttonIndex: Int): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(Pair("buttonIndex", buttonIndex))
    }

}