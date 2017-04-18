package com.opensource.legosdk.nativemodules.pasteboard

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGOPasteboardRequest(val opt: String = "read", val string: String = "", context: LGORequestContext?) : LGORequest(context)