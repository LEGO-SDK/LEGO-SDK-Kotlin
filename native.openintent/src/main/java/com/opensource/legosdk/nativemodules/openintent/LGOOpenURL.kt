package com.opensource.legosdk.nativemodules.openintent

import com.opensource.legosdk.core.*
import com.opensource.legosdk.core.LGOResponse
import android.support.v4.app.ActivityCompat.startActivity
import android.content.Intent
import android.net.Uri
import org.json.JSONObject


/**
 * Created by cuiminghui on 2017/4/21.
 */
class LGOOpenURL: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        return LGOOpenURLOperation(LGOOpenURLRequest(obj.optString("URL", ""), context))
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOOpenURLRequest ?: return null
        return LGOOpenURLOperation(request)
    }

    inner class LGOOpenURLRequest(val URL: String, context: LGORequestContext?): LGORequest(context)

    inner class LGOOpenURLOperation(val request: LGOOpenURLRequest): LGORequestable() {

        override fun requestSynchronize(): LGOResponse {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(request.URL))
                request.context?.requestContentContext()?.startActivity(browserIntent)
                return LGOResponse().accept(null)
            } catch (e: Exception) {
                return LGOResponse().reject("Native.OpenURL", -3, "invalid url.")
            }

        }

    }

}