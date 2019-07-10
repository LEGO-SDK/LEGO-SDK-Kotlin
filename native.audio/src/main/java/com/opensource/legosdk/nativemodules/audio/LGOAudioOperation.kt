package com.opensource.legosdk.nativemodules.audio

import android.content.Context
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import android.os.Vibrator
import com.opensource.legosdk.core.LGOCore.Companion.context



/**
 * Created by Errnull on 2019/7/9.
 */
class LGOAudioOperation(val request: LGOAudioRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request?.let {
            if (it.type.equals("vibrate")) {
                val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(3000)
            }
        }
        val response = LGOAudioResponse()
        return response.accept(null)
    }
}