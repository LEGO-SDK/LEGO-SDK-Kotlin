package com.opensource.legosdk.core

/**
 * Created by PonyCui_Home on 2017/4/14.
 */

class LGORejecting(val errorDomain: String?, val code: Int, val reason: String?): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        return LGOResponse().reject(errorDomain, code, reason)
    }

}