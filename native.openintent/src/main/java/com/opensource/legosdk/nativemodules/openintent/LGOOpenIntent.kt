package com.opensource.legosdk.nativemodules.openintent

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import com.opensource.legosdk.core.*
import org.json.JSONObject


/**
 * Created by cuiminghui on 2017/4/21.
 */
class LGOOpenIntent: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOOpenIntentOperation(LGOOpenIntentRequest(obj.optString("name", ""), obj.optString("action", ""), context));
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOOpenIntentRequest ?: return null
        return LGOOpenIntentOperation(request)
    }

    inner class LGOOpenIntentOperation(val request: LGOOpenIntentRequest): LGORequestable() {

        override fun requestSynchronize(): LGOResponse {
            val intent = Intent()
            val cmp = ComponentName(request.name, request.action)
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            request.context?.requestActivity()?.startActivity(intent)
            return LGOResponse().accept(null)
        }

    }

    inner class LGOOpenIntentRequest(val name: String, val action: String, context: LGORequestContext?) : LGORequest(context)

}