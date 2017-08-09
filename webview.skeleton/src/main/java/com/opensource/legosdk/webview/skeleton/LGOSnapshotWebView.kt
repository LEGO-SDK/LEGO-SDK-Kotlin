package com.opensource.legosdk.webview.skeleton

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebView
import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOWebView
import com.opensource.legosdk.core.LGOWebViewHooker

/**
 * Created by cuiminghui on 2017/8/9.
 */
class LGOSnapshotWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    var snapshotBitmap: Bitmap? = null

    init {
        settings.javaScriptEnabled = true
        settings.allowFileAccess = false
        settings.allowFileAccessFromFileURLs = false
        settings.useWideViewPort = true
    }

    fun loadUrl(url: String?, completionBlock: (bitmap: Bitmap) -> Unit) {
        setWebViewClient(object : LGOWebViewHooker.WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                snapshotBitmap?.let { completionBlock(it) }
                (parent as? ViewGroup)?.let { it.removeView(this@LGOSnapshotWebView) }
            }
        })
        (context as? Activity)?.let {
            it.addContentView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
        loadUrl(url)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val bitmap = snapshotBitmap ?: Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.RGB_565)
            val nCanvas = Canvas(bitmap)
            super.onDraw(nCanvas)
            snapshotBitmap = bitmap
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

}

