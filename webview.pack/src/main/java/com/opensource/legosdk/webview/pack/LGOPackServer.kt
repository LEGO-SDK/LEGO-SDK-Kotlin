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
                System.out.println("LGOPackServer:$filePath")
                if (File(filePath).isDirectory) {
                    try {
                        FileInputStream(File(filePath + "/index.html"))?.let { inputStream ->
                            return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "text/html", inputStream, inputStream.available().toLong())
                        }
                    } catch (e: Exception) {
                        return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, null, null)
                    }
                }
                else {
                    try {
                        FileInputStream(File(filePath))?.let { inputStream ->
                            return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, mimeTypeForFile(uri), inputStream, inputStream.available().toLong())
                        }
                    } catch (e: Exception) {
                        return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, null, null)
                    }
                }
            }
        }
        return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, null, null)
    }

    val customMimeTypes: Map<String, String> = mapOf(
            Pair("css", "text/css"),
            Pair("htm", "text/html"),
            Pair("html", "text/html"),
            Pair("xml", "text/xml"),
            Pair("java", "text/x-java-source, text/java"),
            Pair("md", "text/plain"),
            Pair("txt", "text/plain"),
            Pair("asc", "text/plain"),
            Pair("gif", "image/gif"),
            Pair("jpg", "image/jpeg"),
            Pair("jpeg", "image/jpeg"),
            Pair("png", "image/png"),
            Pair("svg", "image/svg+xml"),
            Pair("mp3", "audio/mpeg"),
            Pair("m3u", "audio/mpeg-url"),
            Pair("mp4", "video/mp4"),
            Pair("ogv", "video/ogg"),
            Pair("flv", "video/x-flv"),
            Pair("mov", "video/quicktime"),
            Pair("swf", "application/x-shockwave-flash"),
            Pair("js", "application/javascript"),
            Pair("pdf", "application/pdf"),
            Pair("doc", "application/msword"),
            Pair("ogg", "application/x-ogg"),
            Pair("zip", "application/octet-stream"),
            Pair("exe", "application/octet-stream"),
            Pair("class", "application/octet-stream"),
            Pair("m3u8", "application/vnd.apple.mpegurl"),
            Pair("ts", "video/mp2t")
    )

    fun mimeTypeForFile(uri: String): String {
        val dot = uri.lastIndexOf('.')
        var mime: String? = null
        if (dot >= 0) {
            mime = customMimeTypes[uri.substring(dot + 1).toLowerCase()]
        }
        return if (mime == null) "application/octet-stream" else mime
    }

}