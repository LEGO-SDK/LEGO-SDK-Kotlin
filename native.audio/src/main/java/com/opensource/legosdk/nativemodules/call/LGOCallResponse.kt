package com.opensource.legosdk.nativemodules.audio

import com.opensource.legosdk.core.LGOResponse
/**
 * Created by Errnull on 2019/7/9.
 */
class LGOAudioResponse: LGOResponse() {

    var SDKVersion = ""
    var audioResult: HashMap<String, Any> = hashMapOf()

    override fun resData(): HashMap<String, Any> {

        return hashMapOf(
                Pair("SDKVersion", SDKVersion),
                Pair("audioResult", audioResult)
        )
    }

}