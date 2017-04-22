package com.opensource.legosdk.uimodules.alertview

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOAlertViewRequest(val title: String?, val message: String?, val buttonTitles: List<String>, context: LGORequestContext?) : LGORequest(context)