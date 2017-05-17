package com.opensource.legosdk.core

import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.webkit.*

/**
 * Created by cuiminghui on 2017/5/16.
 */

class LGOWebViewHooker {

    class HookEntity(val methodName: String, val hookBlock:((p0: Any?, p1: Any?, p2: Any?, p3: Any?) -> Any?)?, val replaceBlock:((p0: Any?, p1: Any?, p2: Any?, p3: Any?) -> Any?)?)

    open class WebViewClient: android.webkit.WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            WebViewClient.hooks["shouldOverrideUrlLoading"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, url, null, null) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, null, null)
                    }
                }
            }
            return false
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            WebViewClient.hooks["shouldOverrideUrlLoading"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, request, null, null) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, request, null, null)
                    }
                }
            }
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            WebViewClient.hooks["onPageStarted"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, url, favicon, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, favicon, null)
                    }
                }
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            WebViewClient.hooks["onPageFinished"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, url, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, null, null)
                    }
                }
            }
            super.onPageFinished(view, url)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            WebViewClient.hooks["onLoadResource"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, url, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, null, null)
                    }
                }
            }
            super.onLoadResource(view, url)
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            WebViewClient.hooks["onPageCommitVisible"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, url, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, null, null)
                    }
                }
            }
            super.onPageCommitVisible(view, url)
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            WebViewClient.hooks["shouldInterceptRequest"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, url, null, null) as? WebResourceResponse)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, null, null)
                    }
                }
            }
            return super.shouldInterceptRequest(view, url)
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            WebViewClient.hooks["shouldInterceptRequest"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, request, null, null) as? WebResourceResponse)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, request, null, null)
                    }
                }
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            WebViewClient.hooks["onReceivedError"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, request, error, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, request, error, null)
                    }
                }
            }
            super.onReceivedError(view, request, error)
        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            WebViewClient.hooks["onReceivedError"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, request, errorResponse, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, request, errorResponse, null)
                    }
                }
            }
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
            WebViewClient.hooks["onFormResubmission"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, dontResend, resend, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, dontResend, resend, null)
                    }
                }
            }
            super.onFormResubmission(view, dontResend, resend)
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            WebViewClient.hooks["doUpdateVisitedHistory"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, url, isReload, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, isReload, null)
                    }
                }
            }
            super.doUpdateVisitedHistory(view, url, isReload)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            WebViewClient.hooks["onReceivedSslError"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, handler, error, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, handler, error, null)
                    }
                }
            }
            super.onReceivedSslError(view, handler, error)
        }

        override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
            WebViewClient.hooks["onReceivedClientCertRequest"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, request, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, request, null, null)
                    }
                }
            }
            super.onReceivedClientCertRequest(view, request)
        }

        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            WebViewClient.hooks["shouldOverrideKeyEvent"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, event, null, null) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, event, null, null)
                    }
                }
            }
            return super.shouldOverrideKeyEvent(view, event)
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            WebViewClient.hooks["onUnhandledKeyEvent"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, event, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, event, null, null)
                    }
                }
            }
            super.onUnhandledKeyEvent(view, event)
        }

        override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
            WebViewClient.hooks["onScaleChanged"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, oldScale, newScale, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, oldScale, newScale, null)
                    }
                }
            }
            super.onScaleChanged(view, oldScale, newScale)
        }

        override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
            WebViewClient.hooks["onReceivedLoginRequest"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, realm, account, args) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, realm, account, args)
                    }
                }
            }
            super.onReceivedLoginRequest(view, realm, account, args)
        }

        companion object {

            val hooks: HashMap<String, MutableList<HookEntity>> = hashMapOf()

            fun addHook(item: HookEntity) {
                if (hooks[item.methodName] == null) {
                    hooks.put(item.methodName, mutableListOf())
                }
                hooks[item.methodName]?.let {
                    it.add(item)
                }
            }

        }

    }

    open class WebChromeClient: android.webkit.WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            WebChromeClient.hooks["onProgressChanged"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, newProgress, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, newProgress, null, null)
                    }
                }
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            WebChromeClient.hooks["onReceivedTitle"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, title, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, title, null, null)
                    }
                }
            }
            super.onReceivedTitle(view, title)
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
            WebChromeClient.hooks["onReceivedTitle"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, icon, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, icon, null, null)
                    }
                }
            }
            super.onReceivedIcon(view, icon)
        }

        override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
            WebChromeClient.hooks["onReceivedTouchIconUrl"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, url, precomposed, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, precomposed, null)
                    }
                }
            }
            super.onReceivedTouchIconUrl(view, url, precomposed)
        }

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            WebChromeClient.hooks["onShowCustomView"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, callback, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, callback, null, null)
                    }
                }
            }
            super.onShowCustomView(view, callback)
        }

        override fun onShowCustomView(view: View?, requestedOrientation: Int, callback: CustomViewCallback?) {
            WebChromeClient.hooks["onShowCustomView"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, requestedOrientation, callback, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, requestedOrientation, callback, null)
                    }
                }
            }
            super.onShowCustomView(view, requestedOrientation, callback)
        }

        override fun onHideCustomView() {
            WebChromeClient.hooks["onHideCustomView"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(null, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(null, null, null, null)
                    }
                }
            }
            super.onHideCustomView()
        }

        override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
            WebChromeClient.hooks["onCreateWindow"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, isDialog, isUserGesture, resultMsg) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, isDialog, isUserGesture, resultMsg)
                    }
                }
            }
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }

        override fun onRequestFocus(view: WebView?) {
            WebChromeClient.hooks["onRequestFocus"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(view, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(view, null, null, null)
                    }
                }
            }
            super.onRequestFocus(view)
        }

        override fun onCloseWindow(window: WebView?) {
            WebChromeClient.hooks["onCloseWindow"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(window, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(window, null, null, null)
                    }
                }
            }
            super.onCloseWindow(window)
        }

        override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            WebChromeClient.hooks["onJsAlert"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, url, message, result) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, message, result)
                    }
                }
            }
            return super.onJsAlert(view, url, message, result)
        }

        override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            WebChromeClient.hooks["onJsConfirm"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, url, message, result) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, message, result)
                    }
                }
            }
            return super.onJsConfirm(view, url, message, result)
        }

        override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
            WebChromeClient.hooks["onJsPrompt"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(url, message, defaultValue, result) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(url, message, defaultValue, result)
                    }
                }
            }
            return super.onJsPrompt(view, url, message, defaultValue, result)
        }

        override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            WebChromeClient.hooks["onJsBeforeUnload"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(view, url, message, result) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(view, url, message, result)
                    }
                }
            }
            return super.onJsBeforeUnload(view, url, message, result)
        }

        override fun onExceededDatabaseQuota(url: String?, databaseIdentifier: String?, quota: Long, estimatedDatabaseSize: Long, totalQuota: Long, quotaUpdater: WebStorage.QuotaUpdater?) {
            WebChromeClient.hooks["onExceededDatabaseQuota"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(url, databaseIdentifier, quota, estimatedDatabaseSize) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(url, databaseIdentifier, quota, estimatedDatabaseSize)
                    }
                }
            }
            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater)
        }

        override fun onReachedMaxAppCacheSize(requiredStorage: Long, quota: Long, quotaUpdater: WebStorage.QuotaUpdater?) {
            WebChromeClient.hooks["onReachedMaxAppCacheSize"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(requiredStorage, quota, quotaUpdater, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(requiredStorage, quota, quotaUpdater, null)
                    }
                }
            }
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater)
        }

        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
            WebChromeClient.hooks["onGeolocationPermissionsShowPrompt"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(origin, callback, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(origin, callback, null, null)
                    }
                }
            }
            super.onGeolocationPermissionsShowPrompt(origin, callback)
        }

        override fun onGeolocationPermissionsHidePrompt() {
            WebChromeClient.hooks["onGeolocationPermissionsHidePrompt"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(null, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(null, null, null, null)
                    }
                }
            }
            super.onGeolocationPermissionsHidePrompt()
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
            WebChromeClient.hooks["onPermissionRequest"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(request, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(request, null, null, null)
                    }
                }
            }
            super.onPermissionRequest(request)
        }

        override fun onPermissionRequestCanceled(request: PermissionRequest?) {
            WebChromeClient.hooks["onPermissionRequestCanceled"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(request, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(request, null, null, null)
                    }
                }
            }
            super.onPermissionRequestCanceled(request)
        }

        override fun onJsTimeout(): Boolean {
            WebChromeClient.hooks["onJsTimeout"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(null, null, null, null) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(null, null, null, null)
                    }
                }
            }
            return super.onJsTimeout()
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            WebChromeClient.hooks["onConsoleMessage"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(consoleMessage, null, null, null) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(consoleMessage, null, null, null)
                    }
                }
            }
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {
            WebChromeClient.hooks["onConsoleMessage"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(message, lineNumber, sourceID, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(message, lineNumber, sourceID, null)
                    }
                }
            }
            super.onConsoleMessage(message, lineNumber, sourceID)
        }

        override fun getDefaultVideoPoster(): Bitmap {
            WebChromeClient.hooks["getDefaultVideoPoster"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(null, null, null, null) as? Bitmap)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(null, null, null, null)
                    }
                }
            }
            return super.getDefaultVideoPoster()
        }

        override fun getVideoLoadingProgressView(): View {
            WebChromeClient.hooks["getVideoLoadingProgressView"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(null, null, null, null) as? View)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(null, null, null, null)
                    }
                }
            }
            return super.getVideoLoadingProgressView()
        }

        override fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {
            WebChromeClient.hooks["getVisitedHistory"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        if (it.invoke(callback, null, null, null) as? Boolean ?: false) return
                    }
                    it.hookBlock?.let {
                        it.invoke(callback, null, null, null)
                    }
                }
            }
            super.getVisitedHistory(callback)
        }

        override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
            WebChromeClient.hooks["onShowFileChooser"]?.let {
                it.forEach {
                    it.replaceBlock?.let {
                        (it.invoke(webView, filePathCallback, fileChooserParams, null) as? Boolean)?.let {
                            return it
                        }
                    }
                    it.hookBlock?.let {
                        it.invoke(webView, filePathCallback, fileChooserParams, null)
                    }
                }
            }
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }

        companion object {

            val hooks: HashMap<String, MutableList<HookEntity>> = hashMapOf()

            fun addHook(item: HookEntity) {
                if (hooks[item.methodName] == null) {
                    hooks.put(item.methodName, mutableListOf())
                }
                hooks[item.methodName]?.let {
                    it.add(item)
                }
            }

        }

    }

}