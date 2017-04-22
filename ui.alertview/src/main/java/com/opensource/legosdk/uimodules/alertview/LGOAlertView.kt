package com.opensource.legosdk.uimodules.alertview

import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOAlertView: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val buttonTitles = mutableListOf<String>()
        obj.optJSONArray("buttonTitles")?.let {
            val length = it.length()
            (0..length - 1).mapTo(buttonTitles) { i -> it.optString(i, "") }
        }
        return LGOAlertViewOperation(LGOAlertViewRequest(
                obj.optString("title"),
                obj.optString("message"),
                buttonTitles.toList(),
                context
        ))
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.AlertView", LGOAlertView())
        }

    }

}