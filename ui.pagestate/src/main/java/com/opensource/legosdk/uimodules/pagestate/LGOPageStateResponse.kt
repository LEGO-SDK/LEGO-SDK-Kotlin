package com.opensource.legosdk.uimodules.pagestate

import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/6/12.
 */
class LGOPageStateResponse(val currentState: String): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(
                Pair("currentState", currentState)
        )
    }

}