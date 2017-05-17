package com.opensource.legosdk.webview.pack

import com.opensource.legosdk.core.LGOCore
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream
import java.net.URI

/**
 * Created by cuiminghui on 2017/5/17.
 */

class LGOPackServer(hostname: String?, port: Int) : NanoHTTPD(hostname, port) {

    fun filePath(url: String): String? {
        try {
            val uri = URI(url)
            return LGOCore.context?.cacheDir?.absolutePath + "/LGOPack_Server/" + uri.path
        } catch (e: Exception) { }
        return null
    }

    override fun serve(session: IHTTPSession?): Response {
        session?.uri?.let { uri ->
            filePath(uri)?.let { filePath ->
                if (File(filePath).isDirectory) {
                    FileInputStream(File(filePath + "/index.html"))?.let { inputStream ->
                        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "text/html", inputStream, inputStream.available().toLong())
                    }
                }
                else {
                    FileInputStream(File(filePath))?.let { inputStream ->
                        return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, getMimeTypeForFile(uri), inputStream, inputStream.available().toLong())
                    }
                }
            }
        }
        return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, null, null)
    }

}