package com.opensource.legosdk.uimodules.refresh

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGORefresh: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGORefreshOperation(LGORefreshRequest(obj.optString("opt", ""), context))
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.Refresh", LGORefresh())
        }

    }

}