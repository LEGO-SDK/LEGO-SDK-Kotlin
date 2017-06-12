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
    var isColumnsRelated: Boolean = false
    var relativeColumnsA: List<String> = listOf()
    var relativeColumnsB: List<List<String>> = listOf()
    var relativeColumnsC: List<List<List<String>>> = listOf()

    fun isEmptyRelativeColumnsB(): Boolean {
        relativeColumnsB.forEach {
            it.forEach {
                if (it.length > 0 && !it.first().equals("/")) {
                    return false
                }
            }
        }
        return true
    }

    fun isEmptyRelativeColumnsC(): Boolean {
        relativeColumnsC.forEach {
            it.forEach {
                it.forEach {
                    if (it.length > 0 && !it.first().equals("/")) {
                        return false
                    }
                }
            }
        }
        return true
    }

}