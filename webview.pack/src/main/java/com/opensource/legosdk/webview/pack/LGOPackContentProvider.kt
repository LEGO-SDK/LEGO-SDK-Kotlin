package com.opensource.legosdk.webview.pack

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.opensource.legosdk.core.LGOCore
import java.io.File

/**
 * Created by cuiminghui on 2017/5/17.
 */

class LGOPackContentProvider: ContentProvider() {

    private fun filePath(uri: Uri): String {
        return LGOCore.context?.cacheDir?.absolutePath + "/LGOPack_Server/" + uri.path
    }

    override fun openFile(uri: Uri?, mode: String?): ParcelFileDescriptor {
        uri?.let { uri ->
            filePath(uri)?.let { filePath ->
                if (File(filePath).isDirectory) {
                    try {
                        File(filePath + "/index.html")?.let { file ->
                            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                        }
                    } catch (e: Exception) { }
                }
                else {
                    try {
                        File(filePath)?.let { file ->
                            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                        }
                    } catch (e: Exception) { }
                }
            }
        }
        return super.openFile(uri, mode)
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        throw UnsupportedOperationException("Not supported by this provider")
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        throw UnsupportedOperationException("Not supported by this provider")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Not supported by this provider")
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Not supported by this provider")
    }

    override fun getType(uri: Uri?): String {
        throw UnsupportedOperationException("Not supported by this provider")
    }

}