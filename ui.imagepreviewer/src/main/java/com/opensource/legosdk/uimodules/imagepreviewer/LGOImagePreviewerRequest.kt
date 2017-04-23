package com.opensource.legosdk.uimodules.imagepreviewer

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOImagePreviewerRequest(val URLs: List<String>, val currentURL: String?, context: LGORequestContext?) : LGORequest(context)