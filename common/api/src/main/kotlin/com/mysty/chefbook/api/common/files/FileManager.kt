package com.mysty.chefbook.api.common.files

import android.content.Context
import java.io.File

interface IFileManager {
    fun getFile(path: String): File
    fun removeFile(path: String): Boolean
}

internal class FileManager(
    private val context: Context,
): IFileManager {

    override fun getFile(path: String): File = File(context.filesDir, path)

    override fun removeFile(path: String): Boolean {
        val file = getFile(path)
        return if (file.exists()) file.deleteRecursively() else false
    }

}
