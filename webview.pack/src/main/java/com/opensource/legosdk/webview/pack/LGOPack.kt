package com.opensource.legosdk.webview.pack

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGOWebViewHooker
import fi.iki.elonen.NanoHTTPD
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.security.MessageDigest
import java.util.zip.ZipInputStream

/**
 * Created by cuiminghui on 2017/5/17.
 */
class LGOPack {

    fun isLocalCached(url: String): Boolean {
        try {
            val uri = URI(url)
            uri.path.split("/").last().takeIf { it.endsWith(".zip") }?.let { zipName ->
                LGOCore.context?.assets?.list("")?.let {
                    return it.contains(zipName)
                }
                cachePath(url)?.let {
                    return File(it).exists()
                }
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    fun cacheKey(url: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(url.toByteArray(charset("UTF-8")))
        val digest = messageDigest.digest()
        val sb = StringBuffer()
        for (b in digest) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    fun cachePath(url: String): String? {
        val baseDir = LGOCore.context?.cacheDir?.absolutePath ?: return null
        return "$baseDir/LGOPack/"+cacheKey(url)+".zip"
    }

    fun serverDocumentPath(url: String): String? {
        val baseDir = LGOCore.context?.cacheDir?.absolutePath ?: return null
        return "$baseDir/LGOPack_Server/"+cacheKey(url)+""
    }

    fun createFileServer(url: String, completionBlock:(localUrl: String) -> Unit) {
        try {
            val serverDocumentPath = serverDocumentPath(url) ?: return
            mkdirs(File(serverDocumentPath))
            val uri = URI(url)
            uri.path.split("/").last().takeIf { it.endsWith(".zip") }?.let { zipName ->
                LGOCore.context?.assets?.list("")?.let {
                    LGOCore.context?.assets?.open(zipName)?.let {
                        ZipInputStream(BufferedInputStream(it))?.let { zipInputStream ->
                            while (true) {
                                val entry = zipInputStream.nextEntry ?: break
                                val filePath = serverDocumentPath + File.separator + entry.name
                                val file = File(filePath)
                                mkdirs(file)
                                if (entry.isDirectory) {
                                    file.mkdirs()
                                } else {
                                    if (file.exists()) {
                                        file.delete()
                                    }
                                    val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
                                    val data = ByteArray(2048)
                                    while (true) {
                                        val count = zipInputStream.read(data, 0, 2048)
                                        if (count < 0) {
                                            break
                                        }
                                        bufferedOutputStream.write(data, 0, count)
                                    }
                                    bufferedOutputStream.close()
                                }
                                zipInputStream.closeEntry()
                            }
                        }
                        it.close()
                        val queryString = if (url.contains("?")) url.split("?").mapIndexedNotNull { index, s -> return@mapIndexedNotNull if (index > 0) s else null }.joinToString("?") else ""
                        completionBlock("http://localhost:$serverPort/"+cacheKey(url)+"/"+queryString)
                    }
                }
                cachePath(url)?.let {

                }
            }
        } catch (e: Exception) {
            print(e)
        }
    }

    private fun mkdirs(file: File) {
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            mkdirs(parentFile)
            parentFile.mkdir()
        }
    }

    companion object {

        val instance = LGOPack()

        var serverPort = 10000
            private set
        var server: LGOPackServer? = null
            private set

        init {
            startService()
            LGOWebViewHooker.WebViewClient.addHook(LGOWebViewHooker.HookEntity("shouldOverrideUrlLoading", null, { p0, p1, p2, p3 ->
                val view = p0 as? WebView ?: return@HookEntity null
                (p1 as? String)?.let { url ->
                    if (url.contains(".zip") && instance.isLocalCached(url)) {
                        instance.createFileServer(url, { it ->
                            view.loadUrl(it)
                        })
                        return@HookEntity true
                    }
                }
                return@HookEntity null
            }))

        }

        fun startService() {
            if (serverPort > 11000) {
                return
            }
            try {
                server = LGOPackServer(null, serverPort)
                server?.start()
            } catch (e: Exception) {
                server = null
                serverPort++
                startService()
            }
        }

    }

}