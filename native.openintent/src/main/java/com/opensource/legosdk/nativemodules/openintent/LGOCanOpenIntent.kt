package com.opensource.legosdk.nativemodules.openintent

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import com.opensource.legosdk.core.*
import org.json.JSONObject


/**
 * Created by cuiminghui on 2017/4/21.
 */
class LGOCanOpenIntent: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOCanOpenIntentOperation(LGOCanOpenIntentRequest(obj.optString("name", ""), obj.optString("action", ""), context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOCanOpenIntentRequest ?: return null
        return LGOCanOpenIntentOperation(request)
    }

    companion object {

        init {
            LGOCore.modules.addModule("Native.CanOpenIntent", LGOCanOpenIntent())
        }

    }

    inner class LGOCanOpenIntentOperation(val request: LGOCanOpenIntentRequest): LGORequestable() {

        override fun requestSynchronize(): LGOResponse {
            val intent = Intent()
            val cmp = ComponentName(request.name, request.action)
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            request.context?.requestContentContext()?.packageManager?.let {
                return if (it.queryIntentActivities(intent, 0).size > 0) LGOResponse().accept(null) else LGOResponse().reject("Native.CanOpenIntent", -1, "Can not open intent.")
            }
            return LGOResponse().reject("Native.CanOpenIntent", -1, "Can not open intent.")
        }

    }

    inner class LGOCanOpenIntentRequest(val name: String, val action: String, context: LGORequestContext?) : LGORequest(context)

}