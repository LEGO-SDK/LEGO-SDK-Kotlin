package com.opensource.legosdk.nativemodules.check

import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOCheckResponse: LGOResponse() {

    var SDKVersion = ""
    var checkResult: HashMap<String, Any> = hashMapOf()

    override fun resData(): HashMap<String, Any> {
        val checkResult = JSONObject()
        this.checkResult.forEach {
            try {
                checkResult.putOpt(it.key, it.value)
            } catch (e: Exception) {}
        }
        return hashMapOf(
                Pair("SDKVersion", SDKVersion),
                Pair("checkResult", checkResult)
        )
    }

}