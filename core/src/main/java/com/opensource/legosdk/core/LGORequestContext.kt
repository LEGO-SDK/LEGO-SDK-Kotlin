package com.opensource.legosdk.core

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView

/**
 * Created by PonyCui_Home on 2017/4/9.
 */

open class LGORequestContext(val sender: Any?) {

    fun runOnMainThread(action: () -> Unit) {
        Handler(requestContentContext()?.mainLooper).post(action)
    }

    fun requestContentContext(): Context? {
        (sender as? WebView)?.let {
            return it.context
        }
        return null
    }

    fun requestActivity(): Activity? {
        (sender as? LGOWebView)?.let {
            it.activity?.let {
                return it
            }
            it.fragment?.activity?.let {
                return it
            }
        }
        (sender as? WebView)?.let {
            return (it.parent as? View)?.context as? Activity
        }
        return null
    }

    fun requestWebView(): LGOWebView? {
        (sender as? LGOWebView)?.let {
            return it
        }
        return null
    }

}
