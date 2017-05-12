package com.opensource.legosdk.uimodules.navigationitem

import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/5/11.
 */

class LGONavigationItem: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGONavigationItemOperation(LGONavigationItemRequest(obj.optString("leftItem"), obj.optString("rightItem"), context))
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.NavigationItem", LGONavigationItem())
        }

    }

}