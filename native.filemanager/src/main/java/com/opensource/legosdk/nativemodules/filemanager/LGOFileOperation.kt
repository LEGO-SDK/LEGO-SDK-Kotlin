package com.opensource.legosdk.nativemodules.filemanager

import android.util.Base64
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset


/**
 * Created by PonyCui_Home on 2017/4/19.
 */
class LGOFileOperation(val request: LGOFileRequest): LGORequestable() {

    fun baseDir(): File? {
        if (request.suite.equals("document", ignoreCase = true)) {
            return request.context?.requestContentContext()?.filesDir
        } else if (request.suite.equals("caches", ignoreCase = true)) {
            return request.context?.requestContentContext()?.cacheDir
        } else if (request.suite.equals("tmp", ignoreCase = true)) {
            return request.context?.requestContentContext()?.cacheDir
        } else {
            return request.context?.requestContentContext()?.cacheDir
        }
    }

    fun localFile(): File? {
        request.filePath?.let {
            val components = it.split("/")
            var relativeDir = ""
            var filePath = "null.txt"
            var i = 0
            for (item in components) {
                if (i >= components.size - 1) {
                    filePath = item
                    break
                } else {
                    i++
                }
                if (item.isEmpty()) {
                    continue
                }
                relativeDir = relativeDir + "/" + item
            }
            baseDir()?.let {
                File(it.absolutePath + "/" + request.suite + "/LGOFileManager/" + relativeDir)?.let {
                    it.mkdirs()
                    return File(it, filePath)
                }
            }
        }
        return null
    }

    override fun requestSynchronize(): LGOResponse {
        localFile()?.let {
            val file = it
            when (request.opt) {
                "update" -> {
                    request.fileContents?.let {
                        val fileContents = it
                        FileOutputStream(file)?.let {
                            if (isValidUTF8(fileContents.toByteArray())) {
                                it.write(fileContents.toByteArray())
                            }
                            else {
                                val data = Base64.decode(fileContents.toByteArray(), 0)
                                it.write(Base64.decode(data, 0))
                            }
                            it.close()
                            return LGOResponse().accept(null)
                        }
                    }
                    return LGOResponse().reject("Native.FileManager", -1, "Could not write to file.")
                }
                "read" -> {
                    FileInputStream(file)?.let {
                        val buffer = ByteArray(it.available())
                        it.read(buffer, 0, it.available())
                        it.close()
                        try {
                            return LGOFileResponse(String(buffer, Charsets.UTF_8))
                        }
                        catch (e: Exception) {
                            return LGOFileResponse(Base64.encodeToString(buffer, 0))
                        }
                    }
                }
                "delete" -> {
                    try {
                        file.delete()
                        return LGOResponse().accept(null)
                    } catch (e: Exception) {
                        return LGOResponse().reject("Native.FileManager", -1, e.localizedMessage)
                    }
                }
                "check" -> {
                    try {
                        if (file.canRead()) {
                            return LGOResponse().accept(hashMapOf(
                                    Pair("exist", true)
                            ))
                        }
                        else {
                            return LGOResponse().accept(hashMapOf(
                                    Pair("exist", false)
                            ))
                        }
                    } catch (e: Exception) {
                        return LGOResponse().reject("Native.FileManager", -1, e.localizedMessage)
                    }
                }
                else -> {
                    return LGOResponse().reject("Native.FileManager", -2, "invalid opt value.")
                }
            }
        }
        return LGOResponse().reject("Native.FileManager", -3, "Permission deny.")
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