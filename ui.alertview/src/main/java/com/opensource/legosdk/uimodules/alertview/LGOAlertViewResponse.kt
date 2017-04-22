package com.opensource.legosdk.uimodules.alertview

import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOAlertViewResponse(val buttonIndex: Int): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(Pair("buttonIndex", buttonIndex))
    }

}