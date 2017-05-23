package com.opensource.legosdk.nativemodules.userdefaults

import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOUserDefaultsResponse(val value: Any?): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        value?.let {
            return hashMapOf(
                    Pair("value", it)
            )
        }
        return hashMapOf()
    }

}