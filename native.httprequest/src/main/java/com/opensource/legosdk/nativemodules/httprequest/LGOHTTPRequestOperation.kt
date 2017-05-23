package com.opensource.legosdk.nativemodules.httprequest

import android.content.res.TypedArray
import android.util.Base64
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset

/**
 * Created by PonyCui_Home on 2017/4/17.
 */
class LGOHTTPRequestOperation(val request: LGOHTTPRequestObject): LGORequestable() {

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        val client = OkHttpClient()
        try {
            var reqBody: RequestBody? = null
            request.data?.let {
                if (it.length > 0) {
                    try {
                        val bytes = Base64.decode(it.toByteArray(), 0)
                        reqBody = RequestBody.create(null, bytes)
                    } catch (e: IllegalArgumentException) {
                        val reqBodyBuilder = FormBody.Builder()
                        it.split("&").forEach {
                            val components = it.split("=")
                            if (components.size == 2) {
                                reqBodyBuilder.addEncoded(components[0], components[1])
                            }
                        }
                        reqBody = reqBodyBuilder.build()
                    }
                }
            }
            val request = Request.Builder()
                    .url(request.URL)
                    .method(if (request.data?.length ?: 0 > 0) "POST" else "GET", reqBody)
                    .headers(Headers.of(request.requestHeaders()))
                    .build()
            Thread({
                val webResponse = client.newCall(request).execute()
                val response = LGOHTTPResponseObject()
                response.responseText = webResponse.body().string()
                response.responseData = webResponse.body().bytes()
                response.statusCode = webResponse.code()
                callbackBlock(response.accept(null))
            }).start()
        } catch (e: Exception) {
            callbackBlock(LGOResponse().reject("Native.HTTPRequest", e.hashCode(), e.localizedMessage))
        }
    }

    fun requestURL(): URL? {
        var relativeURL = request.URL ?: return null
        val webView = request.context?.sender as? WebView ?: return null
        if (!relativeURL.startsWith("http://") && !relativeURL.startsWith("https://")) {
            val uri = URI(webView.url)
            relativeURL = uri.resolve(relativeURL).toString()
        }
        return URL(relativeURL)
    }

    fun isValidUTF8(input: ByteArray): Boolean {
        val cs = Charset.forName("UTF-8").newDecoder()
        try {
            cs.decode(ByteBuffer.wrap(input))
            return true
        } catch (e: CharacterCodingException) {
            return false
        }
    }

}