package com.opensource.legosdk.nativemodules.check

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOCheck: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOCheckOperation()
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        return LGOCheckOperation()
    }

    override fun synchronizeResponse(context: LGORequestContext): LGOResponse? {
        return LGOCheckOperation().requestSynchronize()
    }

}