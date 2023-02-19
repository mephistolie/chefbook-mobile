package com.mysty.chefbook.api.files.data.repositories

import android.webkit.URLUtil
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.files.data.IFileSource
import com.mysty.chefbook.api.files.data.ILocalFileSource
import timber.log.Timber

internal interface IFilesRepo {
    suspend fun getData(path: String) : ActionStatus<ByteArray>
    suspend fun isRemoteSource(path: String): Boolean
    suspend fun compressImage(
        path: String,
        width: Int = 1284,
        height: Int = 1284,
        maxFileSize: Long = 1048576,
        quality: Int = 100,
    ): ActionStatus<String>
}

internal class FilesRepo(
    private val local: ILocalFileSource,
    private val remote: IFileSource,
) : IFilesRepo {

    override suspend fun getData(path: String) : ActionStatus<ByteArray> {
        val result = if (isRemoteSource(path)) {
            remote.getFile(path)
        } else {
            local.getFile(path)
        }
        if (result.isSuccess()) Timber.i( "Got file $path") else Timber.e(result.asFailure().error, "Unable to get file $path")

        return result
    }

    override suspend fun isRemoteSource(path: String): Boolean = URLUtil.isValidUrl(path)
    override suspend fun compressImage(
        path: String,
        width: Int,
        height: Int,
        maxFileSize: Long,
        quality: Int
    ): ActionStatus<String> {
        if (isRemoteSource(path)) {
            Timber.e("Trying to compress remote file $path")
            return ActionStatus.Failure()
        }
        return local.compressImage(
            path = path,
            width = width,
            height = height,
            maxFileSize = maxFileSize,
            quality = quality,
        )
    }

}
