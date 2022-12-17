package com.mysty.chefbook.api.files.data.repositories

import android.webkit.URLUtil
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.files.data.IFileSource
import timber.log.Timber

internal interface IFilesRepo {
    suspend fun getData(path: String) : ActionStatus<ByteArray>
    suspend fun isRemoteSource(path: String): Boolean
}

internal class FilesRepo(
    private val local: IFileSource,
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

}
