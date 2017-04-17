package com.opensource.legosdk.core

import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGOJSMessage(val messageID: String, val moduleName: String, val requestParams: JSONObject, val callbackID: Int) {

    fun call(completionBlock: (metaData: HashMap<String, Any>, resData: HashMap<String, Any>) -> Unit, context: LGORequestContext) {
        LGOCore.modules.moduleWithName(moduleName)?.let {
            it.buildWithJSONObject(requestParams, context)?.let {
                it.requestAsynchronize {
                    assert(it.status != 0, {
                        "Response status still padding."
                    })
                    completionBlock(it.metaData, it.resData())
                }
            }
            return
        }
        val nonModule = LGOResponse().reject("LEGO.SDK", -1, "Module not exists.")
        completionBlock(nonModule.metaData, nonModule.resData())
    }

}