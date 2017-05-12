package com.opensource.legosdk.uimodules.navigationitem

import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/5/12.
 */
class LGONavigationItemResponse(val leftTapped: Boolean, val rightTapped: Boolean): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(
                Pair("leftTapped", leftTapped),
                Pair("rightTapped", rightTapped)
        )
    }

}