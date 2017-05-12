package com.opensource.legosdk.core

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.RelativeLayout

open class LGOWebViewActivity : AppCompatActivity() {

    open var urlString: String? = null
    var pageSetting: Any? = null
    var navigationItems = LGOActionBarController()
    lateinit var webView: LGOWebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationItems.activity = this
        navigationItems.reload()
        intent?.let {
            urlString = it.getStringExtra("LGONavigationController.RequestPath")
        }
        title = null
        webView = LGOWebView(this)
        urlString?.let {
            webView.loadUrl(it)
            applyPageSetting()
        }
        val relativeLayout = RelativeLayout(this)
        relativeLayout.addView(webView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        setContentView(relativeLayout)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        navigationItems.reload()
    }

    override fun onResume() {
        super.onResume()
        applyPageSetting()
    }

    fun applyPageSetting() {
        LGOCore.modules.moduleWithName("UI.Page")?.let {
            val module = it
            try {
                it::class.java.getDeclaredMethod("apply", LGOWebViewActivity::class.java)?.let {
                    it.invoke(module, this)
                }
            } catch (e: Exception) {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            intent?.let {
                if (it.getBooleanExtra("LGONavigationController.Class", false)) {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
