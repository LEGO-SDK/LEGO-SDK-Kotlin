package com.opensource.legosdk.uimodules.navigationitem

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.WebView
import com.opensource.legosdk.core.*
import com.opensource.legosdk.core.LGOCore.Companion.context
import com.opensource.legosdk.core.LGONavigationItem
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

/**
 * Created by cuiminghui on 2017/5/11.
 */
class LGONavigationItemOperation(private val request: LGONavigationItemRequest): LGORequestable() {

    private fun requestURL(url: String): String? {
        var relativeURL = url
        val webView = request.context?.sender as? WebView ?: return null
        if (!relativeURL.startsWith("http://") && !relativeURL.startsWith("https://") && !relativeURL.startsWith("content://")) {
            val uri = URI(webView.url)
            relativeURL = uri.resolve(relativeURL).toString()
        }
        return relativeURL
    }

    fun requestBitmap(url: String, completionBlock: (Bitmap?) -> Unit): Boolean {
        if (url.contains(".png", false)) {
            request.context?.runOnMainThread {
                val URLString = requestURL(url) ?: return@runOnMainThread
                if (URLString.startsWith("content://")) {
                    try {
                        request.context.requestContentContext()?.contentResolver?.openInputStream(Uri.parse(URLString))?.let { inputStream ->
                            BitmapFactory.decodeStream(inputStream)?.let { bitmap ->
                                if (url.contains("@3x.png", true) || url.contains("%403x.png", true)) {
                                    bitmap.density = 3
                                }
                                else {
                                    bitmap.density = 2
                                }
                                completionBlock(bitmap)
                            }
                            inputStream.close()
                        }
                    } catch (e: Exception) {}
                    return@runOnMainThread
                }
                Thread({
                    try {
                        (URL(URLString).openConnection() as? HttpURLConnection)?.let { conn ->
                            conn.connectTimeout = 5000
                            conn.connect()
                            BitmapFactory.decodeStream(conn.inputStream)?.let { bitmap ->
                                if (url.contains("@3x.png", true) || url.contains("%403x.png", true)) {
                                    bitmap.density = 3
                                }
                                else {
                                    bitmap.density = 2
                                }
                                completionBlock(bitmap)
                            }
                            conn.inputStream.close()
                        }
                    } catch (e: Exception) {}
                }).start()
            }
            return true
        }
        if(url.equals("default")){
            completionBlock(requestBackButton2Bitmap())
            return true
        }
        return false
    }

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        request.context?.requestActivity()?.runOnUiThread {
            request.leftItem?.takeIf { it.isNotEmpty() }?.let { leftItem ->
                if (leftItem.equals("null") || leftItem.equals("undefined")) {
                    return@let
                }
                val activity = request.context?.requestActivity() as? LGOWebViewActivity ?: return@let
                if (!requestBitmap(leftItem, { bitmap ->
                            activity.runOnUiThread {
                                activity.navigationItems.leftBarButtonItem = LGONavigationItem.LGOBarButtonItem(null, bitmap, {
                                    callbackBlock(LGONavigationItemResponse(true, false).accept(null))
                                })
                            }
                        })) {
                    activity.navigationItems.leftBarButtonItem = LGONavigationItem.LGOBarButtonItem(leftItem, null, {
                        callbackBlock(LGONavigationItemResponse(true, false).accept(null))
                    })
                }
            }
            request.rightItem?.takeIf { it.isNotEmpty() }?.let { rightItem ->
                if (rightItem.equals("null") || rightItem.equals("undefined")) {
                    return@let
                }
                val activity = request.context?.requestActivity() as? LGOWebViewActivity ?: return@let
                if (!requestBitmap(rightItem, { bitmap ->
                            activity.runOnUiThread {
                                activity.navigationItems.rightBarButtonItem = LGONavigationItem.LGOBarButtonItem(null, bitmap, {
                                    callbackBlock(LGONavigationItemResponse(false, true).accept(null))
                                })
                            }
                        })) {
                    activity.navigationItems.rightBarButtonItem = LGONavigationItem.LGOBarButtonItem(rightItem, null, {
                        callbackBlock(LGONavigationItemResponse(false, true).accept(null))
                    })
                }
            }
        }
    }

}