package com.opensource.legosdk.webview.skeleton

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
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
    var snapshotImageView: ImageView? = null
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
        if (LGOSnapshot.snapshotExists(URL)) {
            LGOSnapshot.fetchSnapshot(URL)?.let {
                val imageView = ImageView(toViewGroup.context)
                imageView.scaleType = ImageView.ScaleType.FIT_START
                imageView.setImageBitmap(it)
                var newLayoutParams = layoutParams
                (layoutParams as? RelativeLayout.LayoutParams)?.let {
                    newLayoutParams = RelativeLayout.LayoutParams(layoutParams.width, layoutParams.height)
                    (newLayoutParams as RelativeLayout.LayoutParams).topMargin = it.topMargin
                    (newLayoutParams as RelativeLayout.LayoutParams).bottomMargin = -it.topMargin
                }
                toViewGroup.addView(imageView, newLayoutParams)
                snapshotImageView = imageView
                return
            }
        }
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
        val animation = AlphaAnimation(1.0f, 0.0f)
        animation.duration = 500
        animation.fillAfter = true
        webView?.let {
            it.startAnimation(animation)
        }
        snapshotImageView?.let {
            it.startAnimation(animation)
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                (webView?.parent as? ViewGroup)?.let { it.removeView(webView) }
                (snapshotImageView?.parent as? ViewGroup)?.let { it.removeView(snapshotImageView) }
            }
            override fun onAnimationStart(animation: Animation?) {}
        })
    }

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        obj.optString("opt")?.let {
            if (it.equals("dismiss")) {
                context.requestActivity()?.runOnUiThread {
                    dismiss(true)
                }
            }
            if (it.equals("snapshot")) {
                val targetURL = obj.optString("targetURL") ?: return LGORequestable.reject("WebView.Skeleton", -1, "targetURL required.")
                val snapshotURL = obj.optString("snapshotURL") ?: return LGORequestable.reject("WebView.Skeleton", -1, "snapshotURL required.")
                LGOSnapshotOperation(LGOSnapshotRequest(targetURL, snapshotURL, context)).start()
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