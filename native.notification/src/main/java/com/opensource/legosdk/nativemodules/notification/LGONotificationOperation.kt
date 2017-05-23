package com.opensource.legosdk.nativemodules.notification

import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGOResponse
import android.R.id.message
import com.opensource.legosdk.nativemodules.notification.LGONotificationOperation.LGONotificationReceiver
import android.content.IntentFilter




/**
 * Created by PonyCui_Home on 2017/4/18.
 */
class LGONotificationOperation(val request: LGONotificationRequest): LGORequestable() {

    companion object {

        val registeredNames: MutableSet<String> = mutableSetOf()

    }

    private fun category(): String {
        request.context?.requestContentContext()?.let {
            val appID = it.getString(it.applicationInfo.labelRes)
            return appID + ".com.lego.sdk.notification"
        }
        return "com.lego.sdk.notification"
    }

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        when (request.opt) {
            "create" -> {
                request.name?.let {
                    val intentFilter = IntentFilter()
                    intentFilter.addCategory(category())
                    intentFilter.addAction(it)
                    request.context?.requestContentContext()?.registerReceiver(
                            LGONotificationReceiver(callbackBlock),
                            intentFilter
                    )
                    registeredNames.add(it)
                }
            }
            "delete" -> {
                request.name?.let {
                    if (it.length > 0) {
                        val intent = Intent();
                        intent.addCategory(category());
                        intent.setAction(request.name);
                        intent.putExtra("context", request.context?.requestContentContext()?.toString())
                        intent.putExtra("remove", true);
                        request.context?.requestContentContext()?.sendBroadcast(intent)
                    }
                    return
                }
                registeredNames.forEach {
                    val intent = Intent()
                    intent.addCategory(category());
                    intent.action = it
                    intent.putExtra("context", request.context?.requestContentContext()?.toString())
                    intent.putExtra("remove", true);
                    request.context?.requestContentContext()?.sendBroadcast(intent)
                }
            }
            "post" -> {
                request.name?.let {
                    val intent = Intent()
                    intent.addCategory(category())
                    intent.action = it
                    intent.putExtra("aPostObject", request.aPostObject)
                    intent.putExtra("aPostUserInfo", request.aPostUserInfo.toString())
                    request.context?.requestContentContext()?.sendBroadcast(intent)
                }
            }
        }
    }

    inner class LGONotificationReceiver(val callbackBlock: (LGOResponse) -> Unit) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getBooleanExtra("remove", false) && intent.getStringExtra("context").equals(context.toString(), ignoreCase = true)) {
                context.unregisterReceiver(this)
                return
            }
            callbackBlock(LGONotificationResponse(intent.getStringExtra("aPostObject"), try {
                JSONObject(intent.getStringExtra("aPostUserInfo"))
            } catch (e: Exception) { null }).accept(null))
        }

    }

}

