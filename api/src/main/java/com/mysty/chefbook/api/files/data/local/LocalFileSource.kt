package com.mysty.chefbook.api.files.data.local

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.errors.FileError
import com.mysty.chefbook.api.common.communication.errors.FileErrorType
import com.mysty.chefbook.api.files.data.IFileSource
import com.mysty.chefbook.core.coroutines.AppDispatchers
import java.io.File
import kotlinx.coroutines.withContext

internal class LocalFileSource(
    private val dispatchers: AppDispatchers,
): IFileSource {

    override suspend fun getFile(path: String): ActionStatus<ByteArray> = withContext(dispatchers.io) {
        val file = File(path)
        if (!file.exists()) return@withContext Failure(FileError(FileErrorType.NOT_EXISTS))
        return@withContext try {
            DataResult(file.readBytes())
        } catch (e: OutOfMemoryError) {
            Failure(FileError(FileErrorType.BIG_FILE))
        }
    }

}
