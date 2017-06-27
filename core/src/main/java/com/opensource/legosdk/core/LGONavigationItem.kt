package com.opensource.legosdk.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

var backButtonImage: Bitmap? = null

inline fun requestBackButtonBitmap(): Bitmap? {
    if (backButtonImage == null) {
        val backButtonIcon = Base64.decode("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", 0)
        BitmapFactory.decodeByteArray(backButtonIcon, 0, backButtonIcon.size)?.let {
            it.density = 3
            backButtonImage = it
        }
    }
    return backButtonImage
}

/**
 * Created by cuiminghui on 2017/5/11.
 */
class LGONavigationItem {

    class LGOBarButtonItem(val title: String?, val image: Bitmap?, val triggerBlock: (() -> Unit)?)

    var activity: LGOWebViewActivity? = null
    var fragment: LGOWebViewFragment? = null

    var hasBackStack: Boolean = false
        get() {
            (activity ?: fragment?.activity)?.let {
                it.intent?.let {
                    return it.getBooleanExtra("LGONavigationController.Class", false)
                }
            }
            return false
        }

    var backBarButtonItem: LGOBarButtonItem = LGOBarButtonItem(null, requestBackButtonBitmap(), {
        activity?.finish()
        fragment?.activity?.finish()
    })
        set(value) {
            field = value
            activity?.navigationBar?.reload()
            fragment?.navigationBar?.reload()
        }


    var leftBarButtonItem: LGOBarButtonItem? = null
        set(value) {
            field = value
            activity?.navigationBar?.reload()
            fragment?.navigationBar?.reload()
        }

    var rightBarButtonItem: LGOBarButtonItem? = null
        set(value) {
            field = value
            activity?.navigationBar?.reload()
            fragment?.navigationBar?.reload()
        }

}