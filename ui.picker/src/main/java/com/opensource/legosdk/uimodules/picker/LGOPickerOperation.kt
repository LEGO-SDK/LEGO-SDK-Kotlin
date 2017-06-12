package com.opensource.legosdk.uimodules.picker

import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse

/**
 * Created by cuiminghui on 2017/5/16.
 */
class LGOPickerOperation(val request: LGOPickerRequest): LGORequestable() {

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        request.context?.runOnMainThread {
            request.context?.requestContentContext()?.let {
                if (request.isColumnsRelated) {
                    val view = OptionsPickerView.Builder(it, OptionsPickerView.OnOptionsSelectListener { options1, options2, options3, _ ->
                        val value1 = if (options1 < request.relativeColumnsA.size) request.relativeColumnsA[options1] else ""
                        val value2 = if (options1 < request.relativeColumnsB.size && options2 < request.relativeColumnsB[options1].size) request.relativeColumnsB[options1][options2] else ""
                        val value3 = if (options1 < request.relativeColumnsC.size && options2 < request.relativeColumnsC[options1].size && options3 < request.relativeColumnsC[options1][options2].size) request.relativeColumnsC[options1][options2][options3] else ""
                        callbackBlock(LGOPickerResponse(listOf(value1, value2, value3), listOf(options1, options2, options3)).accept(null))
                    }).setTitleText(request.title).setCancelText("取消").setSubmitText("确定").build()
                    if (!request.isEmptyRelativeColumnsB() && !request.isEmptyRelativeColumnsC()) {
                        view.setPicker(request.relativeColumnsA, request.relativeColumnsB, request.relativeColumnsC)
                    }
                    else if (!request.isEmptyRelativeColumnsC()) {
                        view.setPicker(request.relativeColumnsA, request.relativeColumnsB)
                    }
                    else {
                        view.setPicker(request.relativeColumnsA)
                    }
                    view.show()
                }
                else {
                    val view = OptionsPickerView.Builder(it, OptionsPickerView.OnOptionsSelectListener { options1, options2, options3, v ->
                        when(request.columns.size) {
                            1 -> {
                                if (options1 < request.columns[0].size) {
                                    callbackBlock(LGOPickerResponse(listOf(request.columns[0][options1]), listOf(options1)).accept(null))
                                }
                            }
                            2 -> {
                                if (options1 < request.columns[0].size && options2 < request.columns[1].size) {
                                    callbackBlock(LGOPickerResponse(listOf(request.columns[0][options1], request.columns[1][options2]), listOf(options1, options2)).accept(null))
                                }
                            }
                            3 -> {
                                if (options1 < request.columns[0].size && options2 < request.columns[1].size && options3 < request.columns[2].size) {
                                    callbackBlock(LGOPickerResponse(listOf(request.columns[0][options1], request.columns[1][options2], request.columns[2][options3]), listOf(options1, options2, options3)).accept(null))
                                }
                            }
                        }
                    }).setTitleText(request.title).setCancelText("取消").setSubmitText("确定").build()
                    when (request.columns.size) {
                        1 -> {
                            val defaultSelected_0 = if (0 < request.defaultValues.size) {
                                request.columns[0].indexOf(request.defaultValues[0])
                            } else -1
                            view.setSelectOptions(defaultSelected_0)
                            view.setNPicker(request.columns[0], null, null)
                        }
                        2 -> {
                            val defaultSelected_0 = if (0 < request.defaultValues.size) {
                                request.columns[0].indexOf(request.defaultValues[0])
                            } else -1
                            val defaultSelected_1 = if (1 < request.defaultValues.size) {
                                request.columns[1].indexOf(request.defaultValues[1])
                            } else -1
                            view.setSelectOptions(defaultSelected_0, defaultSelected_1)
                            view.setNPicker(request.columns[0], request.columns[1], null)
                        }
                        3 -> {
                            val defaultSelected_0 = if (0 < request.defaultValues.size) {
                                request.columns[0].indexOf(request.defaultValues[0])
                            } else -1
                            val defaultSelected_1 = if (1 < request.defaultValues.size) {
                                request.columns[1].indexOf(request.defaultValues[1])
                            } else -1
                            val defaultSelected_2 = if (2 < request.defaultValues.size) {
                                request.columns[2].indexOf(request.defaultValues[2])
                            } else -1
                            view.setSelectOptions(defaultSelected_0, defaultSelected_1, defaultSelected_2)
                            view.setNPicker(request.columns[0], request.columns[1], request.columns[2])
                        }
                    }
                    view.show()
                }
            }
        }
    }

}