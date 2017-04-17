package com.opensource.legosdk.core

/**
 * Created by PonyCui_Home on 2017/4/9.
 */

class LGOModules {

    val items: HashMap<String, LGOModule> = hashMapOf()

    fun addModule(name: String, instance: LGOModule) {
        items[name] = instance
    }

    fun moduleWithName(name: String): LGOModule? {
        return items[name]
    }

    fun allModules(): List<LGOModule> {
        return items.toList().map { return@map it.second }
    }

}