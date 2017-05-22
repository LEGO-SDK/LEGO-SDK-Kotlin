package com.opensource.legosdk.nativemodules.device

import com.opensource.legosdk.core.LGOResponse
import org.json.JSONObject
import android.os.Build
import java.util.*


/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGODeviceResponse : LGOResponse() {

    var deviceName = Build.BRAND
    var deviceModel = Build.MODEL
    var deviceOSName = "Android"
    var deviceOSVersion = Build.VERSION.RELEASE
    var deviceIDFV = ""
    var deviceScreenWidth = 0
    var deviceScreenHeight = 0

    var appName = ""
    var appBundleIdentifier = ""
    var appShortVersion = ""
    var appBuildNumber = 0

    var networkUsingWIFI = false
    var networkCellularType = 0

    override fun resData(): HashMap<String, Any> {
        try {
            val device = JSONObject()
            device.putOpt("name", deviceName)
            device.putOpt("model", deviceModel)
            device.putOpt("osName", deviceOSName)
            device.putOpt("osVersion", deviceOSVersion)
            device.putOpt("identifierForVendor", deviceIDFV)
            device.putOpt("screenWidth", deviceScreenWidth)
            device.putOpt("screenHeight", deviceScreenHeight)
            val app = JSONObject()
            app.putOpt("name", appName)
            app.putOpt("bundleIdentifier", appBundleIdentifier)
            app.putOpt("shortVersion", appShortVersion)
            app.putOpt("buildNumber", appBuildNumber)
            val network = JSONObject()
            network.putOpt("usingWIFI", networkUsingWIFI)
            network.putOpt("cellularType", networkCellularType)
            val custom = JSONObject()
            LGODevice.custom.forEach {
                custom.putOpt(it.key, it.value)
            }
            return hashMapOf(
                    Pair("device", device),
                    Pair("application", app),
                    Pair("network", network),
                    Pair("custom", custom)
            )
        } catch (e: Exception) {
            return hashMapOf()
        }

    }

}