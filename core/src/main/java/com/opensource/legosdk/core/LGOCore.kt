package com.opensource.legosdk.core

import android.content.Context
import android.content.pm.PackageManager
import dalvik.system.DexFile

/**
 * Created by PonyCui_Home on 2017/4/9.
 */
class LGOCore {

    companion object {

        var context: Context? = null
            private set

        val SDKVersion = "1.0.3"

        var whiteList: MutableList<String> = mutableListOf()

        var requireSSL: MutableList<String> = mutableListOf()

        var whiteModule: HashMap<String, MutableList<String>> = hashMapOf()

        val modules = LGOModules()

        var moduleLoaded = false
            private set

        fun loadModules(context: Context) {
            if (moduleLoaded) {
                return
            }
            val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            applicationInfo.metaData.keySet().forEach { moduleName ->
                if (moduleName.startsWith("LGOModule.")) {
                    applicationInfo.metaData.getString(moduleName)?.let {
                        try {
                            val clazz = Class.forName(it)
                            (clazz.getDeclaredConstructor().newInstance() as? LGOModule)?.let {
                                modules.addModule(moduleName.replaceFirst("LGOModule.", ""), it)
                                System.out.println("LGOModule '$moduleName' Loaded.")
                            }
                        } catch (e: Exception) {}
                    }
                }
            }
            moduleLoaded = true
        }

    }


}