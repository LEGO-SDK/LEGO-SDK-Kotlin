package com.opensource.legosdk.webview.skeleton

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.opensource.legosdk.core.LGOCore
import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest

/**
 * Created by cuiminghui on 2017/8/9.
 */
class LGOSnapshotRequest(val targetURL: String, val snapshotURL: String, context: LGORequestContext?) : LGORequest(context)

class LGOSnapshotOperation(val request: LGOSnapshotRequest) {

    fun start() {
        val mutable = queue.toMutableList()
        mutable.add(this)
        queue = mutable.toList()
        next()
    }

    private fun next() {
        if (current == null) {
            queue.firstOrNull()?.let {
                current = it
                it.run()
            }
        }
    }

    private fun markDone() {
        val mutable = queue.toMutableList()
        mutable.remove(this)
        queue = mutable.toList()
        current = null
    }

    private fun run() {
        if (LGOSnapshot.snapshotExists(request.targetURL)) {
            markDone()
            next()
            return
        }
        if (LGOSnapshot.snapshotExists(request.snapshotURL)) {
            LGOSnapshot.fetchSnapshot(request.snapshotURL)?.let {
                save(it)
                markDone()
                next()
                return
            }
        }
        request.context?.requestActivity()?.let {
            it.runOnUiThread {
                LGOSnapshotWebView(it).loadUrl(request.snapshotURL, {
                    save(it)
                    markDone()
                    next()
                })
            }
        }
    }

    private fun save(bitmap: Bitmap) {
        LGOSnapshot.snapshotCachePath(request.targetURL)?.let {
            try {
                File(it).parentFile.mkdirs()
                val outputStream = FileOutputStream(File(it))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) { }
        }
        LGOSnapshot.snapshotCachePath(request.snapshotURL)?.let {
            try {
                File(it).parentFile.mkdirs()
                val outputStream = FileOutputStream(File(it))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) { }
        }
    }

    companion object {

        var queue: List<LGOSnapshotOperation> = listOf()
        var current: LGOSnapshotOperation? = null

    }

}

class LGOSnapshot {

    companion object {

        fun snapshotCacheKey(url: String): String {
            var versionString = ""
            LGOCore.context?.let { context ->
                context.packageManager.getPackageInfo(context.packageName, 0)?.let {
                    versionString = "." + it.versionName + "." + it.versionCode
                }
            }
            val str = "$url.$versionString.png"
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.update(str.toByteArray(charset("UTF-8")))
            val digest = messageDigest.digest()
            val sb = StringBuffer()
            for (b in digest) {
                sb.append(String.format("%02x", b))
            }
            return sb.toString()
        }

        fun snapshotCachePath(url: String): String? {
            val baseDir = LGOCore.context?.cacheDir?.absolutePath ?: return null
            return "$baseDir/LGOSnapshot/" + snapshotCacheKey(url) + ".zip"
        }

        fun snapshotExists(url: String): Boolean {
            snapshotCachePath(url)?.let {
                return File(it).exists()
            }
            return false
        }

        fun fetchSnapshot(url: String): Bitmap? {
            if (snapshotExists(url)) {
                snapshotCachePath(url)?.let {
                    try {
                        return BitmapFactory.decodeStream(FileInputStream(File(it)))
                    } catch (e: Exception) {}
                }
            }
            return null
        }

    }

}