package com.opensource.legosdk.nativemodules.filemanager

import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject


/**
 * Created by PonyCui_Home on 2017/4/19.
 */
class LGOFileManager: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGOFileRequest(context)
        request.suite = obj.optString("suite", "Document")
        request.opt = obj.optString("opt", "read")
        request.filePath = obj.optString("filePath")
        request.filePath?.let {
            request.filePath = it.replace("..", ".")
        }
        request.fileContents = obj.optString("fileContents")
        return LGOFileOperation(request)
    }

    companion object {

        init {
            LGOCore.modules.addModule("Native.FileManager", LGOFileManager())
        }

    }

}