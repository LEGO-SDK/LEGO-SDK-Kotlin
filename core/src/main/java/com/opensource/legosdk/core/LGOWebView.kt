package com.opensource.legosdk.core

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/16.
 */
open class LGOWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    companion object {

        var poolSize = 2

        var pool: List<LGOWebView> = listOf()

        fun requestWebViewFromPool(context: Context): LGOWebView? {
            if (pool.size > 0) {
                val mutable = pool.toMutableList()
                val webView = mutable.first()
                mutable.removeAt(0)
                pool = mutable.toList()
                refillPool(context)
                return webView
            }
            else {
                refillPool(context)
                return null
            }
        }

        fun refillPool(context: Context) {
            if (poolSize - pool.size > 0) {
                val mutable = pool.toMutableList()
                mutable.add(LGOWebView(context))
                pool = mutable.toList()
            }
        }

    }

    var activity: Activity? = null
    var fragment: LGOWebViewFragment? = null
        internal set
    var primaryUrl: String? = null
        private set
    private val webClient = object : LGOWebViewHooker.WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            LGOCore.modules.moduleWithName("WebView.Skeleton")?.let { module ->
                try {
                    module::class.java.getDeclaredMethod("dismiss")?.let {
                        it.invoke(module)
                    }
                } catch (e: Exception) {}
            }
        }
    }
    private val chromeClient = object : LGOWebViewHooker.WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            ((view?.parent as? View)?.context as? LGOWebViewActivity)?.let {
                if (it.pageSetting == null) {
                    it.title = view?.title
                }
            }
        }
    }

    init {
        LGOCore.loadModules(context)
        setWebViewClient(webClient)
        setWebChromeClient(chromeClient)
        settings.javaScriptEnabled = true
        settings.allowFileAccess = false
        settings.allowFileAccessFromFileURLs = false
        settings.useWideViewPort = true
        addJavascriptInterface(this, "JSBridge")
        removeJavascriptInterface("searchBoxJavaBridge_")
        removeJavascriptInterface("accessibility")
        removeJavascriptInterface("accessibilityTraversal")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && isDebug(context)) {
            setWebContentsDebuggingEnabled(true)
        }
        isFocusable = true
        isFocusableInTouchMode = true

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setWebChromeClient(null)
        setWebChromeClient(null)
        removeAllViews()
        clearHistory()
        clearCache(true)
        loadUrl("about:blank")
        destroyDrawingCache()
        destroy()
    }

    override fun loadUrl(url: String?) {
        primaryUrl = url
        if (!webClient.shouldOverrideUrlLoading(this, url)) {
            super.loadUrl(url)
        }
    }

    @android.webkit.JavascriptInterface
    fun exec(body: String?) {
        if (primaryUrl == null || !LGOWatchDog.checkURL(primaryUrl!!) || !LGOWatchDog.checkSSL(primaryUrl!!)) {
            System.out.println("Received an JSMessage request. It's domain not in white list. Request Failed.")
            return
        }
        body?.let {
            JSONObject(it)?.let {
                val callbackID = it.optInt("callbackID", -1)
                val message = LGOJSMessage(
                        it.optString("messageID") ?: "",
                        it.optString("moduleName") ?: "",
                        it.optJSONObject("requestParams") ?: JSONObject(),
                        it.optInt("callbackID", -1)
                )
                if (primaryUrl == null || !LGOWatchDog.checkModule(primaryUrl!!, message.moduleName)) {
                    val response = LGOResponse().reject("LEGO.SDK", -1, "Domain not in module white-list.")
                    callback(message.callbackID, response.metaData, response.resData())
                    return
                }
                message.call({ metaData: HashMap<String, Any>, resData: HashMap<String, Any> ->
                    callback(callbackID, metaData, resData)
                }, LGORequestContext(this))
            }
        }
    }

    fun callback(callbackID: Int, metaData: HashMap<String, Any>, resData: HashMap<String, Any>) {
        val base64MetaString = toBase64(toJSONString(metaData))
        val base64ResString = toBase64(toJSONString(resData))
        val script = "(function(){var JSONMetaString = " +
        "decodeURIComponent(atob('$base64MetaString'));var JSMetaParams = " +
        "JSON.parse(JSONMetaString);var JSONResString = " +
        "decodeURIComponent(atob('$base64ResString'))" +
        ";var JSCallbackParams = " +
        "JSON.parse(JSONResString);" +
        "JSMessageCallbacks[$callbackID].call(" +
        "null, JSMetaParams, JSCallbackParams)})()"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            post({
                evaluateJavascript(script, {})
            })
        }
        else {
            post {
                loadUrl("javascript: " + script)
            }
        }
    }

    fun toJSONString(map: HashMap<String, Any>): String {
        val obj = JSONObject()
        map.forEach {
            try {
                obj.putOpt(it.key, it.value)
            } catch (e: Exception) {}
        }
        return obj.toString(0)
    }

    fun toBase64(str: String): String {
        val data = Uri.encode(str).toByteArray(Charsets.UTF_8)
        return Base64.encodeToString(data, 0).replace("\n", "")
    }

    @android.webkit.JavascriptInterface
    fun bridgeScript(): String {
        if (primaryUrl == null || !LGOWatchDog.checkURL(primaryUrl!!) || !LGOWatchDog.checkSSL(primaryUrl!!)) {
            return ""
        }
        return "window.JSMessageCallbacks=[];window.JSSynchronizeResponses={};window." +
        "JSMessage={newMessage:function(name,requestParams){return{" +
        "messageID:'',moduleName:name,requestParams:requestParams," +
        "callbackID:-1,call:function(callback){if(typeof " +
        "callback=='function'){JSMessageCallbacks.push(callback);this." +
        "callbackID=JSMessageCallbacks.length-1}JSBridge.exec(JSON." +
        "stringify(this));if(JSSynchronizeResponses[this.moduleName]!==" +
        "undefined){return JSSynchronizeResponses[this.moduleName]}}}}};" + argsScript() + syncScript()
    }

    fun argsScript(): String {
        (activity as? LGOWebViewActivity)?.args?.let {
            return "window._args = {}; try { window._args = JSON.parse(decodeURIComponent(atob('" + toBase64(it.toString()) + "'))); }catch(e){}"
        }
        return ""
    }

    fun syncScript(): String {
        return LGOCore.modules.items.mapNotNull {
            val moduleName = it.key
            it.value.synchronizeResponse(LGORequestContext(this))?.let {
                val base64ResString = toBase64(toJSONString(it.resData()))
                return@mapNotNull "JSSynchronizeResponses['$moduleName'] = JSON.parse(decodeURIComponent(atob('$base64ResString')))"
            }
        }.joinToString(";")
    }

    fun isDebug(context: Context): Boolean {
        try {
            val clazz = Class.forName(context.packageName + ".BuildConfig")
            val field = clazz.getField("DEBUG")
            return field.get(null) as? Boolean ?: false
        } catch (e: Exception) { }
        return false
    }

    @android.webkit.JavascriptInterface
    fun showKeyboard() {
        (LGOCore.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let {
            it.showSoftInput(this, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}