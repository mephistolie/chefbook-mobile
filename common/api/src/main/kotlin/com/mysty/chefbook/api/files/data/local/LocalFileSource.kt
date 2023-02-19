package com.mysty.chefbook.api.files.data.local

import android.content.Context
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.errors.FileError
import com.mysty.chefbook.api.common.communication.errors.FileErrorType
import com.mysty.chefbook.api.files.data.ILocalFileSource
import com.mysty.chefbook.core.coroutines.AppDispatchers
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File
import java.util.UUID
import kotlinx.coroutines.withContext

internal class LocalFileSource(
    private val dispatchers: AppDispatchers,
    private val context: Context,
): ILocalFileSource {

    override suspend fun getFile(path: String): ActionStatus<ByteArray> = withContext(dispatchers.io) {
        val file = File(path)
        if (!file.exists()) return@withContext Failure(FileError(FileErrorType.NOT_EXISTS))
        return@withContext try {
            DataResult(file.readBytes())
        } catch (e: OutOfMemoryError) {
            Failure(FileError(FileErrorType.BIG_FILE))
        }
    }

    override suspend fun compressImage(
        path: String,
        width: Int,
        height: Int,
        maxFileSize: Long,
        quality: Int,
    ): ActionStatus<String> = withContext(dispatchers.io) {
        val file = File(path)

        file.nameWithoutExtension
        val compressedFile =
            Compressor.compress(context, file) {
                resolution(width, height)
                size(maxFileSize)
                quality(quality)
                destination(File( "${file.parent}/${file.nameWithoutExtension}_tmp_${UUID.randomUUID()}.jpg"))
            }
        return@withContext DataResult(compressedFile.path)
    }

}
