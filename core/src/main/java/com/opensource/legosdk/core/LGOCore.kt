package com.opensource.legosdk.core

import android.content.Context
import dalvik.system.DexFile

/**
 * Created by PonyCui_Home on 2017/4/9.
 */
class LGOCore {

    companion object {

        val SDKVersion = "0.3.0"

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
            try {
                DexFile(context.packageCodePath)?.let {
                    for (item in it.entries()) {
                        if (item.startsWith("com.opensource.legosdk.")) {
                            Class.forName(item)
                        }
                    }
                }
            } catch (e: Exception) {}
            moduleLoaded = true
        }

    }


}