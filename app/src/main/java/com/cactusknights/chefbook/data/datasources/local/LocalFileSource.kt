package com.cactusknights.chefbook.data.datasources.local

import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.FileError
import com.cactusknights.chefbook.domain.entities.action.FileErrorType
import java.io.File
import javax.inject.Inject

class LocalFileSource @Inject constructor(): IFileSource {

    override suspend fun getFile(path: String): ActionStatus<ByteArray> {
        val file = File(path)
        if (!file.exists()) return Failure(FileError(FileErrorType.NOT_EXISTS))
        return try {
            DataResult(file.readBytes())
        } catch (e: OutOfMemoryError) {
            Failure(FileError(FileErrorType.BIG_FILE))
        }
    }

}
