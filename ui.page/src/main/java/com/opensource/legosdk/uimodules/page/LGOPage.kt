package com.opensource.legosdk.uimodules.page

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.WindowManager
import com.opensource.legosdk.core.*
import org.json.JSONObject
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.view.View


/**
 * Created by cuiminghui on 2017/4/23.
 */
class LGOPage: LGOModule() {

    val settings: HashMap<String, LGOPageRequest> = hashMapOf()

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val request = LGOPageRequest(context)
        request.urlPattern = obj.optString("urlPattern")
        request.title = obj.optString("title")
        request.backgroundColor = obj.optString("backgroundColor")
        request.statusBarHidden = obj.optBoolean("statusBarHidden", false)
        request.navigationBarHidden = obj.optBoolean("navigationBarHidden", false)
        request.navigationBarSeparatorHidden = obj.optBoolean("navigationBarSeparatorHidden", false)
        request.navigationBarBackgroundColor = obj.optString("navigationBarBackgroundColor")
        request.navigationBarTintColor = obj.optString("navigationBarTintColor")
        request.fullScreenContent = obj.optBoolean("fullScreenContent", false)
        request.showsIndicator = obj.optBoolean("showsIndicator", true)
        return LGOPageOperation(request)
    }

    fun apply(activity: LGOWebViewActivity) {
        activity.runOnUiThread {
            activity.webView.url?.let {
                val url = it
                settings.entries.forEach {
                    val request = it.value
                    if (it.key.equals(url)) {
                        activity.pageSetting = request
                    }
                    else {
                        Regex(it.key)?.let {
                            it.matchEntire(url)?.let {
                                if (it.range.start == 0 && it.range.endInclusive == url.length - 1) {
                                    activity.pageSetting = request
                                }
                            }
                        }
                    }
                }
            }
            (activity.pageSetting as? LGOPageRequest)?.let {
                activity.title = it.title
                it.backgroundColor?.takeIf(String::isNotEmpty)?.let {
                    activity.webView.setBackgroundColor(Color.parseColor(it))
                }
                if (it.statusBarHidden) {
                    activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
                else {
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
                if (it.navigationBarHidden) {
                    activity.supportActionBar?.let(android.support.v7.app.ActionBar::hide)
                }
                else {
                    activity.supportActionBar?.let(android.support.v7.app.ActionBar::show)
                }
                if (it.navigationBarSeparatorHidden) {
                    activity.supportActionBar?.elevation = 0f
                }
                else {
                    activity.supportActionBar?.elevation = 5f
                }
                it.navigationBarBackgroundColor?.takeIf(String::isNotEmpty)?.let {
                    activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(it)))
                }
                it.navigationBarTintColor?.takeIf(String::isNotEmpty)?.let {
                    activity.navigationItems.tintColor = Color.parseColor(it)
                }
                if (it.fullScreenContent) {
                    activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                }
                else {
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                }
            }
        }
    }

    companion object {

        init {
            LGOCore.modules.addModule("UI.Page", LGOPage())
        }

    }

}