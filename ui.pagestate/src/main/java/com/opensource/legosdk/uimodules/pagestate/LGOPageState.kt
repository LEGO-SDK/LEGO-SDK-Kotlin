package com.opensource.legosdk.uimodules.pagestate

import com.opensource.legosdk.core.*
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/6/12.
 */
class LGOPageState: LGOModule() {

    inner class LGOPageStateOperation(val request: LGORequest): LGORequestable() {

        override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
            request.context?.requestActivity()?.let { activity ->
                (activity as? LGOWebViewActivity)?.let { activity ->
                    activity.addHook({
                        callbackBlock(LGOPageStateResponse("active"))
                        callbackBlock(LGOPageStateResponse("appear"))
                    }, "onResume")
                    activity.addHook({
                        callbackBlock(LGOPageStateResponse("disappear"))
                        callbackBlock(LGOPageStateResponse("inactive"))
                    }, "onPause")
                }
            }
        }

    }

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOPageStateOperation(LGORequest(context))
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.PageState", LGOPageState())
        }

    }

}