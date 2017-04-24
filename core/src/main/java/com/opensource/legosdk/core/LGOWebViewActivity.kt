package com.opensource.legosdk.core

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

open class LGOWebViewActivity : AppCompatActivity() {

    open var urlString: String? = null
    var pageSetting: Any? = null
    lateinit var webView: LGOWebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = null
        webView = LGOWebView(this)
        intent?.let {
            urlString = intent.getStringExtra("LGONavigationController.RequestPath")
        }
        urlString?.let {
            webView.loadUrl(it)
            applyPageSetting()
        }
        val relativeLayout = RelativeLayout(this)
        relativeLayout.addView(webView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        setContentView(relativeLayout)
    }

    override fun onResume() {
        super.onResume()
        applyPageSetting()
    }

    fun applyPageSetting() {
        LGOCore.modules.moduleWithName("UI.Page")?.let {
            val module = it
            it::class.java.getDeclaredMethod("apply", LGOWebViewActivity::class.java)?.let {
                it.invoke(module, this)
            }
        }
    }

}
