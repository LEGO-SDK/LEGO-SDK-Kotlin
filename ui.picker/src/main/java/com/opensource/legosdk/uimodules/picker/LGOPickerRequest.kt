package com.opensource.legosdk.uimodules.picker

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by cuiminghui on 2017/5/16.
 */
class LGOPickerRequest(context: LGORequestContext?) : LGORequest(context) {

    var title: String? = null
    var columnTitles: List<String> = listOf()
    var columns: List<List<String>> = listOf()
    var defaultValues: List<String> = listOf()

}