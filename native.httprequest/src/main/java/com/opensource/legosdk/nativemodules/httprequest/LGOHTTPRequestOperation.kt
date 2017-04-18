package com.opensource.legosdk.nativemodules.httprequest

import android.content.res.TypedArray
import android.util.Base64
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
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
        val BUFFER_SIZE = 4096
        val thread = Thread({
            var statusCode = 0
            var responseMessage = ""
            requestURL()?.let {
                try {
                    (it.openConnection() as? HttpURLConnection)?.let {
                        val conn = it
                        conn.connectTimeout = request.timeout * 1000
                        request.data?.let {
                            if (it.length > 0) {
                                conn.requestMethod = "POST"
                            }
                        }
                        request.headers?.let {
                            it.keys().forEach {
                                conn.setRequestProperty(it, request.headers?.optString(it))
                            }
                        }
                        conn.connect()
                        request.data?.let {
                            if (it.length > 0) {
                                try {
                                    val os = conn.outputStream
                                    os.write(Base64.decode(it.toByteArray(), 0))
                                    os.close()
                                } catch (e: IllegalArgumentException) {
                                    val os = conn.outputStream
                                    os.write(it.toByteArray())
                                    os.close()
                                }
                            }
                        }
                        statusCode = conn.responseCode
                        responseMessage = conn.responseMessage
                        val inputStream = conn.inputStream
                        val outputStream = ByteArrayOutputStream()
                        val buffer = ByteArray(BUFFER_SIZE)
                        var count: Int = 0
                        while (true) {
                            count = inputStream.read(buffer, 0, BUFFER_SIZE)
                            if (count == -1) {
                                break
                            }
                            outputStream.write(buffer, 0, count)
                        }
                        val response = LGOHTTPResponseObject()
                        response.responseText = if(isValidUTF8(outputStream.toByteArray())) String(outputStream.toByteArray(), Charsets.UTF_8) else ""
                        response.responseData = outputStream.toByteArray()
                        response.statusCode = statusCode
                        callbackBlock(response.accept(null))
                        inputStream.close()
                    }
                }
                catch (e: IOException) {
                    callbackBlock(LGOResponse().reject("Native.HTTPRequest", -statusCode, e.localizedMessage))
                }
            }
        })
        thread.start()
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