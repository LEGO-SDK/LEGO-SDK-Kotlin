package com.opensource.legosdk.nativemodules.check

import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import android.R.id.message
import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule



/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOCheckOperation: LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        val response = LGOCheckResponse()
        response.SDKVersion = LGOCore.SDKVersion
        LGOCore.modules.items.forEach {
            response.checkResult.put(it.key, it.value.ver)
        }
        return response.accept(null)
    }

}