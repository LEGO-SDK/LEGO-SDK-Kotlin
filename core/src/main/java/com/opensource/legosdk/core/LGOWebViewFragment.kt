package com.opensource.legosdk.core

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import org.json.JSONObject

/**
 * Created by cuiminghui on 2017/6/27.
 */
open class LGOWebViewFragment: Fragment() {

    companion object {
        var navigationBarDrawable: Drawable? = null
    }

    var contentView: ViewGroup? = null
    var navigationItems = LGONavigationItem()
    var args: JSONObject? = null
    private var hooks: Map<String, List<() -> Unit>> = hashMapOf()
    lateinit var navigationBar: LGONavigationBar
    lateinit var webView: LGOWebView

    fun addHook(hookBlock: () -> Unit, forMethod: String) {
        val mutableHooks = hooks.toMutableMap()
        val hookTarget = mutableHooks[forMethod]?.toMutableList() ?: mutableListOf()
        hookTarget.add(hookBlock)
        mutableHooks[forMethod] = hookTarget.toList()
        hooks = mutableHooks.toMap()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navigationBar = LGONavigationBar(context)
        navigationBar.fragment = this
        navigationItems.fragment = this
        navigationBar.reload()
        webView = LGOWebView.requestWebViewFromPool(context) ?:LGOWebView(context)
        webView.fragment = this
        webView.activity = this.activity
        resetLayouts()
        hooks["onCreate"]?.forEach { it.invoke() }
        return contentView
    }

    override fun onResume() {
        super.onResume()
        hooks["onResume"]?.forEach { it.invoke() }
    }

    override fun onPause() {
        super.onPause()
        hooks["onPause"]?.forEach { it.invoke() }
    }

    override fun onStop() {
        super.onStop()
        hooks["onStop"]?.forEach { it.invoke() }
    }

    override fun onDestroy() {
        super.onDestroy()
        hooks["onDestroy"]?.forEach { it.invoke() }
    }

    open var title: String? = null
        set(value) {
            field = value
            navigationBar.reload()
        }

    open var urlString: String? = null
        set(value) {
            field = value
            value?.let {
                webView.loadUrl(it)
                applyPageSetting()
            }
        }

    var usingCustomPageSetting: Boolean = false
    var pageSetting: Any? = null

    fun applyPageSetting() {
        LGOCore.modules.moduleWithName("UI.Page")?.let {
            val module = it
            try {
                it::class.java.getDeclaredMethod("apply", LGOWebViewFragment::class.java)?.let {
                    it.invoke(module, this)
                }
            } catch (e: Exception) {}
        }
    }

    fun resetLayouts() {
        if (contentView == null) {
            contentView = RelativeLayout(context)
        }
        (contentView?.getChildAt(0) as? ViewGroup)?.let {
            it.removeAllViews()
        }
        contentView?.removeAllViews()
        if (!navigationBar.hidden) {
            if (navigationBar.statusBarTranslucent) {
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(context)
                val webViewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                layout.addView(webView, webViewParams)
                val navigationBarParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (68 * resources.displayMetrics.density).toInt())
                layout.addView(navigationBar, navigationBarParams)
                contentView?.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(context)
                val webViewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                webViewParams.topMargin = (48 * resources.displayMetrics.density).toInt()
                layout.addView(webView, webViewParams)
                val navigationBarParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (48 * resources.displayMetrics.density).toInt())
                layout.addView(navigationBar, navigationBarParams)
                contentView?.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        }
        else {
            if (navigationBar.statusBarTranslucent) {
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(context)
                val webViewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                layout.addView(webView, webViewParams)
                contentView?.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(context)
                layout.addView(webView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))
                contentView?.addView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == webView.uploadFileChooseRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let { uri ->
                    webView.uploadFileChooseCallback?.let {
                        it.onReceiveValue(arrayOf(uri))
                        return
                    }
                }
                webView.uploadFileChooseCallback?.let {
                    it.onReceiveValue(null)
                }
            } else {
                webView.uploadFileChooseCallback?.let {
                    it.onReceiveValue(null)
                }
            }
        }
    }

}