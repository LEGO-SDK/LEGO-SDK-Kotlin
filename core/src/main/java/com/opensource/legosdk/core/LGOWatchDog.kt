package com.opensource.legosdk.core

import java.net.URI

/**
 * Created by cuiminghui on 2017/5/18.
 */
class LGOWatchDog {

    companion object {

        fun checkURL(url: String): Boolean {
            if (LGOCore.whiteList.count() == 0) {
                return true
            }
            if (url.startsWith("file:///android_asset/")) {
                return true
            }
            try {
                val uri = URI(url)
                uri.host?.let { host ->
                    LGOCore.whiteList.forEach {
                        if (host.endsWith(it, true)) {
                            return true
                        }
                    }
                }

            } catch (e: Exception) { }
            return false
        }

        fun checkSSL(url: String): Boolean {
            if (LGOCore.whiteList.count() == 0) {
                return true
            }
            if (url.startsWith("file:///android_asset/")) {
                return true
            }
            try {
                val uri = URI(url)
                uri.host?.let { host ->
                    LGOCore.requireSSL.forEach {
                        if (host.endsWith(it, true)) {
                            if (uri.scheme == null || !uri.scheme.equals("https", true)) {
                                return false
                            }
                        }
                    }
                    return true
                }
            } catch (e: Exception) { }
            return false
        }

        fun checkModule(url: String, moduleName: String): Boolean {
            if (!checkURL(url)) {
                return false
            }
            try {
                val uri = URI(url)
                if (uri.host == null && LGOCore.whiteList.count() == 0) {
                    return true
                }
                if (url.startsWith("file:///android_asset/")) {
                    return true
                }
                else if (uri.host != null) {
                    if (LGOCore.whiteModule[moduleName] == null) {
                        return true
                    }
                    LGOCore.whiteModule[moduleName]?.let { moduleSettings ->
                        moduleSettings.forEach { moduleSetting ->
                            if (uri.host.endsWith(moduleSetting)) {
                                return true
                            }
                        }
                    }
                }
            } catch (e: Exception) { }
            return false
        }

    }

}