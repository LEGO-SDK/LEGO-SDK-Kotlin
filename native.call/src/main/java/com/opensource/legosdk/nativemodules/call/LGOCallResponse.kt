package com.opensource.legosdk.nativemodules.call

import com.opensource.legosdk.core.LGOResponse
/**
 * Created by Errnull on 2019/7/9.
 */
class LGOCallResponse: LGOResponse() {

    var SDKVersion = ""
    var callResult: HashMap<String, Any> = hashMapOf()

    override fun resData(): HashMap<String, Any> {

        return hashMapOf(
                Pair("SDKVersion", SDKVersion),
                Pair("callResult", callResult)
        )
    }

}