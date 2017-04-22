package com.opensource.legosdk.uimodules.actionsheet

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by cuiminghui on 2017/4/22.
 */

class LGOActionSheetRequest(val title: String?, val buttonTitles: List<String>, context: LGORequestContext?) : LGORequest(context)