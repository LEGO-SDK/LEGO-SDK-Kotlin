package com.opensource.legosdk.nativemodules.audio

/**
 * Created by Errnull on 2019/7/9.
 */

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

class LGOAudioRequest(val methodName: String?, val userInfo: Map<String, String>, context: LGORequestContext?) : LGORequest(context)