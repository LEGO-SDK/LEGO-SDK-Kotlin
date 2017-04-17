package com.opensource.legosdk.core

/**
 * Created by PonyCui_Home on 2017/4/9.
 */
open class LGORequestable {

    open fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        callbackBlock(requestSynchronize())
    }

    open fun requestSynchronize(): LGOResponse {
        return LGOResponse()
    }

    companion object {

        fun reject(domain: String, code: Int, reason: String): LGORequestable {
            return LGORejecting(domain, code, reason)
        }

    }

}