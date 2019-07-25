package com.opensource.legosdk.nativemodules.httprequest

import android.net.Uri
import android.util.Base64
import android.webkit.WebView
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
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
class LGOHTTPRequestOperation(private val request: LGOHTTPRequestObject): LGORequestable() {

    override fun requestAsynchronize(callbackBlock: (LGOResponse) -> Unit) {
        val BUFFER_SIZE = 4096
        request.context?.runOnMainThread {
            val URLString = requestURL() ?: return@runOnMainThread
            if (URLString.startsWith("content://")) {
                try {
                    request.context.requestContentContext()?.contentResolver?.openFileDescriptor(Uri.parse(URLString), "r")?.let {
                        val inputStream = FileInputStream(it.fileDescriptor)
                        val outputStream = ByteArrayOutputStream()
                        val buffer = ByteArray(BUFFER_SIZE)
                        var count: Int
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
                        response.statusCode = 200
                        callbackBlock(response.accept(null))
                        inputStream.close()
                    }
                } catch (e: Exception) {
                    callbackBlock(LGOResponse().reject("Native.HTTPRequest", -404, e.localizedMessage))
                }
                return@runOnMainThread
            } else if (URLString.startsWith("file:///android_asset")) {
                request.context.requestContentContext()?.assets?.let {
                    val inputStream = it.open("oa_A002/css/base.css")
                    val outputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(BUFFER_SIZE)
                    var count: Int
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
                    response.statusCode = 200
                    callbackBlock(response.accept(null))
                    inputStream.close()
                }
            }
            val thread = Thread({
                var statusCode = 0
                URL(URLString).let {
                    try {
                        (it.openConnection() as? HttpURLConnection)?.let {
                            val conn = it
                            conn.connectTimeout = request.timeout * 1000
                            request.data?.let {
                                if (it.isNotEmpty()) {
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
                                if (it.isNotEmpty()) {
                                    try {
                                        if (checkBase64(it)) {
                                            val os = conn.outputStream
                                            os.write(Base64.decode(it, Base64.DEFAULT))
                                            os.close()
                                        } else {
                                            val os = conn.outputStream
                                            os.write(it.toByteArray())
                                            os.close()
                                        }
                                    } catch (e: Exception) {
                                        val os = conn.outputStream
                                        os.write(it.toByteArray())
                                        os.close()
                                    }
                                }
                            }
                            statusCode = conn.responseCode
                            val inputStream = conn.inputStream
                            val outputStream = ByteArrayOutputStream()
                            val buffer = ByteArray(BUFFER_SIZE)
                            var count: Int
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

    }

    private fun requestURL(): String? {
        var relativeURL = request.URL ?: return null
        val webView = request.context?.sender as? WebView ?: return null
        if (!relativeURL.startsWith("http://") && !relativeURL.startsWith("https://") && !relativeURL.startsWith("content://")&& !relativeURL.startsWith("file://")) {
            val uri = URI(webView.url)
            relativeURL = uri.resolve(relativeURL).toString()
        }
        return relativeURL
    }

    private fun isValidUTF8(input: ByteArray): Boolean {
        val cs = Charset.forName("UTF-8").newDecoder()
        return try {
            cs.decode(ByteBuffer.wrap(input))
            true
        } catch (e: CharacterCodingException) {
            false
        }
    }

    fun checkBase64(str: String): Boolean {
        if (str.length % 4 != 0) {
            return false
        }
        val charArray = str.toCharArray()
        for (i in charArray.indices) {
            if (charArray[i] >= 'A' && charArray[i] <= 'Z') {
                continue
            }
            if (charArray[i] >= 'a' && charArray[i] <= 'z') {
                continue
            }
            if (charArray[i] >= '0' && charArray[i] <= '9') {
                continue
            }
            if (charArray[i] == '+' || charArray[i] == '\\' || charArray[i] == '=') {
                continue
            }
            return false
        }
        return true
    }

}