package com.opensource.legosdk.nativemodules.notification

import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject


/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGONotificationOperation(val request: LGONotificationRequest): LGORequestable() {

    inner class ObserverEntity(val receiver: String, val name: String, val triggerBlock:(aPostObject: String?, aPostUserInfo: JSONObject?) -> Unit)

    companion object {

        var observers: List<ObserverEntity> = listOf()

    }

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        when (request.opt) {
            "create" -> {
                request.name?.let { name ->
                    request.context?.requestActivity()?.let { receiver ->
                        val mutableList = observers.toMutableList()
                        mutableList.add(ObserverEntity(receiver.toString(), name, { a, b ->
                            callbackBlock(LGONotificationResponse(a, b).accept(null))
                        }))
                        observers = mutableList.toList()
                    }
                }
            }
            "delete" -> {
                request.name?.takeIf { it.length > 0 }?.let { name ->
                    request.context?.requestActivity()?.let { receiver ->
                        observers = observers.filter {
                            return@filter !(it.name.equals(name, false) && it.receiver.equals(receiver.toString()))
                        }
                        return
                    }
                }
                request.context?.requestActivity()?.let { receiver ->
                    observers = observers.filter {
                        return@filter !it.receiver.equals(receiver)
                    }
                }
            }
            "post" -> {
                request.name?.takeIf { it.length > 0 }?.let { name ->
                    observers.filter { it.name.equals(name) }.forEach {
                        it.triggerBlock(request.aPostObject, request.aPostUserInfo)
                    }
                }
            }
        }
    }

}

