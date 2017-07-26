package com.opensource.legosdk.webview.skeleton

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOModule
import com.opensource.legosdk.core.LGORequestContext
import com.opensource.legosdk.core.LGORequestable
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/7/25.
 */
class LGOSkeleton: LGOModule() {

    var skeletonNotExists = false
    var skeletonLoaded = false
    var webView: WebView? = null
    var handleDismiss: Boolean = false

    fun loadSkeleton(context: Context) {
        try {
            val inputStream = context.assets.open("skeleton.html")
            val byteArray = ByteArray(inputStream.available())
            inputStream.read(byteArray, 0, inputStream.available())
            inputStream.close()
            if (webView == null) {
                webView = WebView(context)
                webView?.let {
                    it.settings.useWideViewPort = true
                    it.settings.javaScriptEnabled = true
                    it.settings.loadWithOverviewMode = true
                }
            }
            webView?.loadData(String(byteArray), "text/html", "utf-8")
            skeletonLoaded = true
        } catch (e: Exception) {
            skeletonNotExists = true
        }
    }

    fun attachSkeleton(toViewGroup: ViewGroup, layoutParams: ViewGroup.LayoutParams, URL: String) {
        if (skeletonNotExists) {
            return
        }
        if (!skeletonLoaded) {
            loadSkeleton(toViewGroup.context)
        }
        if (skeletonLoaded) {
            (webView?.parent as? ViewGroup)?.let { it.removeView(webView) }
            handleDismiss = false
            webView?.evaluateJavascript("handleRequest('$URL')", { value ->
                value?.takeIf { it.equals("true", true) }?.let {
                    handleDismiss = true
                }
            })
            toViewGroup.addView(webView, layoutParams)
            webView?.evaluateJavascript("canHandleRequest('$URL')", { value ->
                var unhandled = true
                value?.takeIf { it.equals("true", true) }?.let {
                    unhandled = false
                }
                if (unhandled) {
                    (webView?.parent as? ViewGroup)?.let { it.removeView(webView) }
                }
            })
        }
    }

    fun dismiss() {
        dismiss(false)
    }

    fun dismiss(force: Boolean) {
        if (!force && handleDismiss) {
            return
        }
        (webView?.parent as? ViewGroup)?.let { it.removeView(webView) }
    }

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        obj.optString("opt")?.let {
            if (it.equals("dismiss")) {
                context.requestActivity()?.runOnUiThread {
                    dismiss(true)
                }
            }
        }
        return LGORequestable()
    }

    companion object {

        init {
            LGOCore.modules.addModule("WebView.Skeleton", LGOSkeleton())
        }

    }

}