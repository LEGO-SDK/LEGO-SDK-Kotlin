package com.opensource.legosdk.uimodules.navigationitem

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by cuiminghui on 2017/5/11.
 */

class LGONavigationItemRequest(val leftItem: String?, val rightItem: String?, context: LGORequestContext?) : LGORequest(context)

