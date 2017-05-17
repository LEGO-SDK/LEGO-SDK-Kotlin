package com.opensource.legosdk.webview.pack

import android.util.Base64
import com.opensource.legosdk.core.LGOCore
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.net.URL
import java.security.Key
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * Created by cuiminghui on 2017/5/17.
 */
class LGOPackDownloader {

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

    fun updateFile(url: String) {
        requestMD5(url)?.let {
            print(it)
        }
    }

    fun requestMD5(url: String): String? {
        val BUFFER_SIZE = 512
        val url = url + ".hash?_t=" + Date().time
        val publicKey = requestPublicKey(url)?.replace("-----BEGIN PUBLIC KEY-----", "")?.replace("-----END PUBLIC KEY-----", "")?.replace(Regex("\n"), "") ?: return null
        try {
            URL(url).openConnection()?.let {
                it.connect()
                val inputStream = it.inputStream
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
                val cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC")
                cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(Base64.decode(publicKey, 0))))
                return String(cipher.doFinal(Base64.decode(outputStream.toByteArray(), 0)))
            }
        } catch (e: Exception) {
            print(e)
        }
        return null
    }

    fun requestPublicKey(url: String): String? {
        LGOPack.sharedPublicKeys.forEach {
            if (url.startsWith(it.key, true)) {
                return it.value
            }
        }
        LGOPack.sharedPublicKeys.forEach {
            val host = try {
                URI(url).host
            } catch (e: Exception) { null } ?: return@forEach
            if (host.equals(it.key, true)) {
                return it.value
            }
        }
        return null
    }

    fun downloadFile(url: String) {
        try {
            URL(url).openConnection()?.let {
                it.connect()
                val file = File(cachePath(url))
                if (file.exists()) {
                    file.delete()
                }
                val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
                val data = ByteArray(2048)
                while (true) {
                    val count = it.getInputStream().read(data, 0, 2048)
                    if (count < 0) {
                        break

                    }
                    bufferedOutputStream.write(data, 0, count)
                }
                bufferedOutputStream.close()
            }
        } catch (e: Exception) {}
    }

}