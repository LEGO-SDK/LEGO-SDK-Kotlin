package com.opensource.legosdk.core

/**
 * Created by PonyCui_Home on 2017/4/9.
 */
open class LGOResponse {

    var status = 0
        private set

    var metaData: HashMap<String, Any> = hashMapOf()

    fun reject(errorDomain: String?, errorCode: Int, errorReason: String?): LGOResponse {
        assert(status == 0, {
            return@assert "Response has been accepted or rejected."
        })
        status = -1
        metaData = hashMapOf(
                Pair("error", true),
                Pair("module", errorDomain ?: "LEGO.SDK"),
                Pair("code", errorCode),
                Pair("reason", errorReason ?: "unknown error.")
        )
        return this
    }

    fun accept(metaData: HashMap<String, Any>?): LGOResponse {
        assert(status == 0, {
            return@assert "Response has been accepted or rejected."
        });
        metaData?.let { this.metaData = it }
        return this
    }

    open fun resData(): HashMap<String, Any> {
        return hashMapOf()
    }

}