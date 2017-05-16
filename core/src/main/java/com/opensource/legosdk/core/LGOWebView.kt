package com.opensource.legosdk.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.util.Base64
import android.webkit.*
import org.json.JSONObject

/**
 * Created by PonyCui_Home on 2017/4/16.
 */
class LGOWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        LGOCore.loadModules(context)
        setWebViewClient(object : LGOWebViewHooker.WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                (context as? LGOWebViewActivity)?.let {
                    if (it.pageSetting == null) {
                        it.title = view?.title
                    }
                }
            }
        })
        setWebChromeClient(object : WebChromeClient() {

        })
        settings.javaScriptEnabled = true
        addJavascriptInterface(this, "JSBridge")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setWebContentsDebuggingEnabled(true)
        }
    }

    @android.webkit.JavascriptInterface
    fun exec(body: String?) {
        body?.let {
            JSONObject(it)?.let {
                val callbackID = it.optInt("callbackID", -1)
                val message = LGOJSMessage(
                        it.optString("messageID") ?: "",
                        it.optString("moduleName") ?: "",
                        it.optJSONObject("requestParams") ?: JSONObject(),
                        it.optInt("callbackID", -1)
                )
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
        return "var JSMessageCallbacks=[];var JSSynchronizeResponses={};var " +
        "JSMessage={newMessage:function(name,requestParams){return{" +
        "messageID:'',moduleName:name,requestParams:requestParams," +
        "callbackID:-1,call:function(callback){if(typeof " +
        "callback=='function'){JSMessageCallbacks.push(callback);this." +
        "callbackID=JSMessageCallbacks.length-1}JSBridge.exec(JSON." +
        "stringify(this));if(JSSynchronizeResponses[this.moduleName]!==" +
        "undefined){return JSSynchronizeResponses[this.moduleName]}}}}};" + argsScript() + syncScript()
    }

    fun argsScript(): String {
        (context as? LGOWebViewActivity)?.args?.let {
            return "window._args = {}; Object.assign(window._args, JSON.parse(decodeURIComponent(atob('" + toBase64(it.toString()) + "'))));"
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

}