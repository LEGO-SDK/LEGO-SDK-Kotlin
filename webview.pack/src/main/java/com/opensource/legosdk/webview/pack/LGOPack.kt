package com.opensource.legosdk.webview.pack

import android.os.Build
import android.webkit.WebResourceRequest
import com.opensource.legosdk.core.*
import java.io.*
import java.net.URI
import java.security.MessageDigest
import java.util.zip.ZipInputStream

/**
 * Created by cuiminghui on 2017/5/17.
 */
class LGOPack: LGOModule() {

    init {
        LGOPack.addHook()
    }

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
        val url = url.split("?").first()
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
        var versionString = ""
        LGOCore.context?.let { context ->
            context.packageManager.getPackageInfo(context.packageName, 0)?.let {
                versionString = "." + it.versionName + "." + it.versionCode
            }
        }
        return "$baseDir/LGOPack/" + cacheKey(url) + versionString +  ".zip"
    }

    fun serverDocumentPath(url: String): String? {
        val baseDir = LGOCore.context?.cacheDir?.absolutePath ?: return null
        return "$baseDir/LGOPack_Server/"+cacheKey(url)+""
    }

    fun createFileServer(url: String, completionBlock:(localUrl: String) -> Unit) {
        try {
            if (!LGOCore.whiteList.contains(LGOPackContentProvider.serverDomain(LGOCore.context!!))) {
                LGOCore.whiteList.add(LGOPackContentProvider.serverDomain(LGOCore.context!!))
            }
            val serverDocumentPath = serverDocumentPath(url) ?: return
            mkdirs(File(serverDocumentPath))
            val uri = URI(url)
            uri.path.split("/").last().takeIf { it.endsWith(".zip") }?.let { zipName ->
                try {
                    cachePath(url)?.let {
                        val fileInputStream = FileInputStream(File(it))
                        ZipInputStream(BufferedInputStream(fileInputStream))?.let { zipInputStream ->
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
                        fileInputStream.close()
                        val queryString = if (url.contains("?")) url.split("?").mapIndexedNotNull { index, s -> return@mapIndexedNotNull if (index > 0) s else null }.joinToString("?") else ""
                        return completionBlock("content://" + LGOPackContentProvider.serverDomain(LGOCore.context!!) + "/"+cacheKey(url)+"/"+queryString)
                    }
                } catch (e: Exception) {
                    print(e)
                }
                try {
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
                            return completionBlock("content://" + LGOPackContentProvider.serverDomain(LGOCore.context!!) + "/"+cacheKey(url)+"/"+queryString)
                        }
                    }
                } catch (e: Exception) {
                    print(e)
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

        val sharedInstance = LGOPack()
        val sharedDownloader = LGOPackDownloader()

        var sharedPublicKeys: HashMap<String, String> = hashMapOf()

        fun addHook() {
            LGOWebViewHooker.WebViewClient.addHook(LGOWebViewHooker.HookEntity("shouldOverrideUrlLoading", null, { p0, p1, p2, p3 ->
                val view = p0 as? LGOWebView ?: return@HookEntity null
                (p1 as? String)?.let { url ->
                    if (url.contains(".zip") && LGOWatchDog.checkURL(url) && LGOWatchDog.checkSSL(url)) {
                        Thread({
                            sharedInstance.createFileServer(url, { it ->
                                view.activity?.runOnUiThread {
                                    view.loadUrl(it)
                                }
                            })
                            val noCache = !sharedInstance.isLocalCached(url)
                            sharedDownloader.updateFile(url)
                            if (noCache) {
                                sharedInstance.createFileServer(url, { it ->
                                    view.activity?.runOnUiThread {
                                        view.loadUrl(it)
                                    }
                                })
                            }
                        }).start()
                        return@HookEntity true
                    }
                }
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    (p1 as? WebResourceRequest)?.let { request ->
                        val url = request.url?.toString() ?: return@HookEntity null
                        if (url.contains(".zip") && LGOWatchDog.checkURL(url) && LGOWatchDog.checkSSL(url)) {
                            Thread({
                                sharedInstance.createFileServer(url, { it ->
                                    view.activity?.runOnUiThread {
                                        view.loadUrl(it)
                                    }
                                })
                                val noCache = !sharedInstance.isLocalCached(url)
                                sharedDownloader.updateFile(url)
                                if (noCache) {
                                    sharedInstance.createFileServer(url, { it ->
                                        view.activity?.runOnUiThread {
                                            view.loadUrl(it)
                                        }
                                    })
                                }
                            }).start()
                            return@HookEntity true
                        }
                    }
                }
                return@HookEntity null
            }))

        }

        fun setPublicKey(publicKey: String, uri: String) {
            sharedPublicKeys.put(uri, publicKey)
        }

        fun setPublicKey(inputStream: InputStream, uri: String) {
            try {
                val buffer = ByteArray(inputStream.available())
                inputStream.read(buffer, 0, inputStream.available())
                inputStream.close()
                sharedPublicKeys.put(uri, String(buffer, Charsets.UTF_8))
            }
            catch (e: Exception) { }
        }

    }

}