package com.opensource.legosdk.uimodules.alertview

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOAlertView: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val buttonTitles = mutableListOf<String>()
        obj.optJSONArray("buttonTitles")?.let {
            val length = it.length()
            (0 until length).mapTo(buttonTitles) { i -> it.optString(i, "") }
        }
        return LGOAlertViewOperation(LGOAlertViewRequest(
                obj.optString("title"),
                obj.optString("message"),
                buttonTitles.toList(),
                context
        ))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOAlertViewRequest ?: return null
        return LGOAlertViewOperation(request)
    }

}