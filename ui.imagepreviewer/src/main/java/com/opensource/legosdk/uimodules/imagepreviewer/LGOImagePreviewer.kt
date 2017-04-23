package com.opensource.legosdk.uimodules.imagepreviewer

import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOImagePreviewer: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val URLs = mutableListOf<String>()
        obj.optJSONArray("URLs")?.let {
            val length = it.length()
            (0..length - 1).mapTo(URLs) { i -> it.optString(i, "") }
        }
        return LGOImagePreviewerOperation(LGOImagePreviewerRequest(URLs.toList(), obj.optString("currentURL"), context))
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.ImagePreviewer", LGOImagePreviewer())
        }

    }

}