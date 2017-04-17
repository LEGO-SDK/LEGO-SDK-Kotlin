package com.opensource.legosdk.core

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

    }


}