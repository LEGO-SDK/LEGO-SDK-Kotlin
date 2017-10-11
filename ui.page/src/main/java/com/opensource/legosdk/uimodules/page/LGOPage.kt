package com.opensource.legosdk.uimodules.page

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.ActionBar
import android.view.WindowManager
import com.opensource.legosdk.core.*
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by cuiminghui on 2017/4/23.
 */
class LGOPage: LGOModule() {

    var settings: Map<String, LGOPageRequest> = mapOf()

    override fun buildWithJSONObject(obj: JSONObject, context: LGORequestContext): LGORequestable? {
        val items = obj.optJSONArray("items")
        if (items != null) {
            return LGOPageOperation((0 until items.length()).mapNotNull { idx ->
                items.optJSONObject(idx)?.let { obj ->
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
                    return@mapNotNull request
                }
                return@mapNotNull null
            })
        }
        else {
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
            return LGOPageOperation(listOf(request))
        }
    }

    override fun buildWithRequest(request: LGORequest): LGORequestable? {
        val request = request as? LGOPageRequest ?: return null
        return LGOPageOperation(listOf(request))
    }

    fun apply(activity: LGOWebViewActivity) {
        activity.runOnUiThread {
            if (!activity.usingCustomPageSetting) {
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
                activity.navigationBar.hidden = it.navigationBarHidden
                it.navigationBarBackgroundColor?.takeIf(String::isNotEmpty)?.let {
                    activity.navigationBar.barTintColor = Color.parseColor(it)
                }
                it.navigationBarTintColor?.takeIf(String::isNotEmpty)?.let {
                    activity.navigationBar.tintColor = Color.parseColor(it)
                }
                activity.navigationBar.statusBarTranslucent = it.fullScreenContent
            }
        }
    }

    fun apply(fragment: LGOWebViewFragment) {
        fragment.activity.runOnUiThread {
            if (!fragment.usingCustomPageSetting) {
                fragment.webView.url?.let {
                    val url = it
                    settings.entries.forEach {
                        val request = it.value
                        if (it.key.equals(url)) {
                            fragment.pageSetting = request
                        }
                        else {
                            Regex(it.key)?.let {
                                it.matchEntire(url)?.let {
                                    if (it.range.start == 0 && it.range.endInclusive == url.length - 1) {
                                        fragment.pageSetting = request
                                    }
                                }
                            }
                        }
                    }
                }
            }
            (fragment.pageSetting as? LGOPageRequest)?.let {
                fragment.title = it.title
                it.backgroundColor?.takeIf(String::isNotEmpty)?.let {
                    fragment.webView.setBackgroundColor(Color.parseColor(it))
                }
                if (it.statusBarHidden) {
                    fragment.activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
                else {
                    fragment.activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
                fragment.navigationBar.hidden = it.navigationBarHidden
                it.navigationBarBackgroundColor?.takeIf(String::isNotEmpty)?.let {
                    fragment.navigationBar.barTintColor = Color.parseColor(it)
                }
                it.navigationBarTintColor?.takeIf(String::isNotEmpty)?.let {
                    fragment.navigationBar.tintColor = Color.parseColor(it)
                }
                fragment.navigationBar.statusBarTranslucent = it.fullScreenContent
            }
        }
    }

}