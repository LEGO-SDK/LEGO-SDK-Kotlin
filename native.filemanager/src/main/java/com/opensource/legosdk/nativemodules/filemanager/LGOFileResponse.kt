package com.opensource.legosdk.nativemodules.filemanager

import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject



/**
 * Created by PonyCui_Home on 2017/4/19.
 */
class LGOFileResponse(var fileContents: String?): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        fileContents?.let {
            return hashMapOf(
                    Pair("fileContents", it)
            )
        }
        return hashMapOf()
    }

}