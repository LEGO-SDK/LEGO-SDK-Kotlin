package com.opensource.legosdk.nativemodules.call

import android.content.Intent
import android.net.Uri
import com.opensource.legosdk.core.LGOCore.Companion.context
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse


/**
 * Created by Errnull on 2019/7/9.
 */
class LGOCallOperation(val request: LGOCallRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request?.let {
            if (it.methodName.equals("tel:")) {
                var intent = Intent()
                intent.action = Intent.ACTION_DIAL
                intent.data = Uri.parse("tel:" + it.userInfo.get("number"))
                context?.startActivity(intent)
            }
        }
        val response = LGOCallResponse()
        return response.accept(null)
    }

}