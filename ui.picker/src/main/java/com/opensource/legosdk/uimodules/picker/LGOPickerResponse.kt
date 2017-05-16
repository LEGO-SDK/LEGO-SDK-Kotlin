package com.opensource.legosdk.uimodules.picker

import com.opensource.legosdk.core.LGOResponse
import org.json.JSONArray

/**
 * Created by cuiminghui on 2017/5/16.
 */
class LGOPickerResponse(val selectedValues: List<String>, val selectedIndexes: List<Int>): LGOResponse() {

    override fun resData(): HashMap<String, Any> {
        return hashMapOf(
                Pair("selectedValues", JSONArray(selectedValues)),
                Pair("selectedIndexes", JSONArray(selectedIndexes))
        )
    }

}