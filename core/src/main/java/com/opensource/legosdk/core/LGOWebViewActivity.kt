package com.opensource.legosdk.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import org.json.JSONObject

open class LGOWebViewActivity : Activity() {

    companion object {

        var navigationBarDrawable: Drawable? = null

        fun openURL(context: Context, url: String) {
            val intent = Intent(context, LGOWebViewActivity::class.java)
            intent.putExtra("LGONavigationController.RequestPath", url)
            context.startActivity(intent)
        }

    }

    var contentView: ViewGroup? = null
    open var urlString: String? = null
    var usingCustomPageSetting: Boolean = false
    var pageSetting: Any? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        navigationBar = LGONavigationBar(this)
        navigationItems.activity = this
        intent?.let {
            urlString = it.getStringExtra("LGONavigationController.RequestPath") ?: it.getStringExtra("LGOModalController.RequestPath")
            (it.getStringExtra("LGONavigationController.args") ?: it.getStringExtra("LGOModalController.args"))?.let {
                args = try {
                    JSONObject(it)
                } catch (e: Exception) { null }
            }
        }
        title = null
        var preloadWebView: LGOWebView? = null
        (intent?.getStringExtra("LGONavigationController.preloadToken") ?: intent?.getStringExtra("LGOModalController.preloadToken"))?.let { preloadToken ->
            LGOCore.modules.moduleWithName("WebView.Preload")?.let { module ->
                try {
                    module::class.java.getDeclaredMethod("fetchWebView", String::class.java)?.let {
                        (it.invoke(module, preloadToken) as? LGOWebView)?.let {
                            preloadWebView = it
                        }
                    }
                } catch (e: Exception) {}
            }
        }
        webView = preloadWebView ?: LGOWebView.requestWebViewFromPool(this) ?: LGOWebView(this)
        webView.activity = this
        urlString?.let {
            webView.loadUrl(it)
            applyPageSetting()
        }
        resetLayouts()
        hooks["onCreate"]?.forEach { it.invoke() }
    }

    fun resetLayouts() {
        contentView?.removeAllViews()
        if (!navigationBar.hidden) {
            if (navigationBar.statusBarTranslucent) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(this)
                val webViewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                layout.addView(webView, webViewParams)
                LGOCore.modules.moduleWithName("WebView.Skeleton")?.let { module ->
                    try {
                        module::class.java.getDeclaredMethod("attachSkeleton", ViewGroup::class.java, ViewGroup.LayoutParams::class.java, String::class.java)?.let {
                            it.invoke(module, layout, webViewParams, this.urlString ?: "")
                        }
                    } catch (e: Exception) {}
                }
                val navigationBarParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (68 * resources.displayMetrics.density).toInt())
                layout.addView(navigationBar, navigationBarParams)
                contentView = layout
            }
            else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(this)
                val webViewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                webViewParams.topMargin = (48 * resources.displayMetrics.density).toInt()
                layout.addView(webView, webViewParams)
                LGOCore.modules.moduleWithName("WebView.Skeleton")?.let { module ->
                    try {
                        module::class.java.getDeclaredMethod("attachSkeleton", ViewGroup::class.java, ViewGroup.LayoutParams::class.java, String::class.java)?.let {
                            it.invoke(module, layout, webViewParams, this.urlString ?: "")
                        }
                    } catch (e: Exception) {}
                }
                val navigationBarParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (48 * resources.displayMetrics.density).toInt())
                layout.addView(navigationBar, navigationBarParams)
                contentView = layout
            }
        }
        else {
            if (navigationBar.statusBarTranslucent) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(this)
                val webViewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                layout.addView(webView, webViewParams)
                LGOCore.modules.moduleWithName("WebView.Skeleton")?.let { module ->
                    try {
                        module::class.java.getDeclaredMethod("attachSkeleton", ViewGroup::class.java, ViewGroup.LayoutParams::class.java, String::class.java)?.let {
                            it.invoke(module, layout, webViewParams, this.urlString ?: "")
                        }
                    } catch (e: Exception) {}
                }
                contentView = layout
            }
            else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                val layout = RelativeLayout(this)
                layout.addView(webView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))
                LGOCore.modules.moduleWithName("WebView.Skeleton")?.let { module ->
                    try {
                        module::class.java.getDeclaredMethod("attachSkeleton", ViewGroup::class.java, ViewGroup.LayoutParams::class.java, String::class.java)?.let {
                            it.invoke(module, layout, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT), this.urlString ?: "")
                        }
                    } catch (e: Exception) {}
                }
                contentView = layout
            }
        }
        setContentView(contentView)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        navigationBar.reload()
    }

    override fun onResume() {
        super.onResume()
        applyPageSetting()
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
                else if (it.getBooleanExtra("LGOModalController.Class", false)) {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
