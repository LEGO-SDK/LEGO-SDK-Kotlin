package com.opensource.legosdk.nativemodules.device

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.view.WindowManager
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import java.util.*


/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGODeviceOperation(val context: LGORequestContext): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        val context = context.requestContentContext() ?: return LGOResponse().reject("LGODevice", -2, "no context.")
        val response = LGODeviceResponse()
        response.deviceIDFV = IDFV()
        response.deviceScreenWidth = screenSize().x
        response.deviceScreenHeight = screenSize().y
        response.appName = context.getString(context.applicationInfo.labelRes)
        response.appBundleIdentifier = context.applicationInfo.packageName
        context.packageManager.getPackageInfo(context.packageName, 0)?.let {
            response.appShortVersion = it.versionName
            response.appBuildNumber = it.versionCode
        }
        response.networkUsingWIFI = usingWIFI()
        response.networkCellularType = cellularType()
        return response.accept(null)
    }

    fun IDFV(): String {
        try {
            val context = context.requestContentContext() ?: return ""
            val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
            deviceId?.let {
                return UUID.nameUUIDFromBytes(it.toByteArray(charset("utf8"))).toString()
            }
        } catch (e: Exception) { }
        return ""
    }

    fun screenSize(): Point {
        val context = context.requestContentContext() ?: return Point(0, 0)
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    fun usingWIFI(): Boolean {
        val context = context.requestContentContext() ?: return false
        val permission = "android.permission.ACCESS_NETWORK_STATE"
        val res = context.checkCallingOrSelfPermission(permission)
        if (res != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
            it.activeNetworkInfo?.let {
                return it.type == ConnectivityManager.TYPE_WIFI
            }
        }
        return false
    }

    fun cellularType(): Int {
        val context = context.requestContentContext() ?: return 0
        val permission = "android.permission.ACCESS_NETWORK_STATE"
        val res = context.checkCallingOrSelfPermission(permission)
        if (res != PackageManager.PERMISSION_GRANTED) {
            return 0
        }
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
            it.activeNetworkInfo?.let {
                val subType = it.subtype
                when (subType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, // 联通2g
                        TelephonyManager.NETWORK_TYPE_CDMA, // 电信2g
                        TelephonyManager.NETWORK_TYPE_EDGE, // 移动2g
                        TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN -> return 2
                        TelephonyManager.NETWORK_TYPE_EVDO_A, // 电信3g
                        TelephonyManager.NETWORK_TYPE_UMTS,
                        TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA,
                        TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP -> return 3
                        TelephonyManager.NETWORK_TYPE_LTE -> return 4
                    else -> return 3
                }
            }
        }
        return 0
    }

}