package com.opensource.legosdk.uimodules.picker

import com.opensource.legosdk.core.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/5/16.
 */
class LGOPicker: LGOModule() {

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGOPickerRequest(context)
        request.title = obj.optString("title")
        obj.optJSONArray("columnTitles")?.let {
            request.columnTitles = (0 until it.length()).map { idx ->
                return@map it.optString(idx, "")
            }
        }
        obj.optJSONArray("columns")?.let { rowData ->
            request.columns = (0 until rowData.length()).map {
                (rowData.get(it) as? JSONArray)?.let {
                    return@map (0 until it.length()).map { idx ->
                        return@map it.optString(idx, "")
                    }
                }
                return@map listOf<String>()
            }
        }
        obj.optJSONArray("defaultValues")?.let {
            request.defaultValues = (0 until it.length()).map { idx ->
                return@map it.optString(idx, "")
            }
        }
        return LGOPickerOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOPickerRequest ?: return null
        return LGOPickerOperation(request)
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.Picker", LGOPicker())
        }

    }

}