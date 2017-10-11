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
        request.isColumnsRelated = obj.optBoolean("isColumnsRelated", false)
        if (request.isColumnsRelated) {
            obj.optJSONArray("columns")?.let { columns ->
                request.relativeColumnsA = (0 until columns.length()).map {
                    columns.optJSONObject(it)?.let {
                        return@map it.optString("title", "/")
                    }
                    return@map columns.optString(it, "/")
                }
                request.relativeColumnsB = (0 until columns.length()).map {
                    columns.optJSONObject(it)?.let {
                        it.optJSONArray("item")?.let { item ->
                            return@map (0 until item.length())?.map {
                                item.optJSONObject(it)?.let {
                                    it.optString("title")?.let {
                                        return@map it
                                    }
                                }
                                return@map "/"
                            }
                        }
                    }
                    return@map listOf<String>()
                }
                request.relativeColumnsC = (0 until columns.length()).map {
                    columns.optJSONObject(it)?.let {
                        it.optJSONArray("item")?.let { item ->
                            return@map (0 until item.length())?.map {
                                item.optJSONObject(it)?.let {
                                    it.optJSONArray("item")?.let { subItem ->
                                        return@map (0 until subItem.length()).map {
                                            subItem.optJSONObject(it)?.let {
                                                it.optString("title")?.let {
                                                    return@map it
                                                }
                                            }
                                            return@map subItem.optString(it, "/")
                                        }
                                    }
                                }
                                return@map listOf("")
                            }
                        }
                    }
                    return@map listOf(listOf(""))
                }
            }
        }
        return LGOPickerOperation(request)
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOPickerRequest ?: return null
        return LGOPickerOperation(request)
    }

}