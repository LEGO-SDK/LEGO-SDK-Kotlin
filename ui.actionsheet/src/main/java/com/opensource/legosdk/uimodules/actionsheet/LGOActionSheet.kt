package com.opensource.legosdk.uimodules.actionsheet

import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject
import org.json.JSONArray



/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOActionSheet: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        obj.optJSONArray("buttonTitles")?.let {
            val length = it.length()
            val buttonTitles = mutableListOf<String>()
            (0..length - 1).mapTo(buttonTitles) { i -> it.optString(i, "") }
            return LGOActionSheetOperation(LGOActionSheetRequest(obj.optString("title"), buttonTitles.toList(), context))
        }
        return null
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.ActionSheet", LGOActionSheet())
        }

    }

}