package com.opensource.legosdk.core

import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/9.
 */
open class LGOModule {

    open val isSynchronize = false

    open val ver = 0

    open fun buildWithRequest(request: LGORequest): LGORequestable? {
        return null
    }

    open fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return null
    }

    open fun synchronizeResponse(): LGOResponse? {
        return null
    }

}