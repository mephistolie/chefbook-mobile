package com.cactusknights.chefbook.data.repositories

import android.webkit.URLUtil
import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import timber.log.Timber

interface IFileRepo {
    suspend fun getFile(path: String) : ActionStatus<ByteArray>
    suspend fun isOnlineSource(path: String): Boolean
}

class FileRepo(
    private val local: IFileSource,
    private val remote: IFileSource,
) : IFileRepo {

    override suspend fun getFile(path: String) : ActionStatus<ByteArray> {
        val result = if (isOnlineSource(path)) {
            remote.getFile(path)
        } else {
            local.getFile(path)
        }
        if (result.isSuccess()) Timber.i( "Got file $path") else Timber.e(result.asFailure().error, "Unable to get file $path")

        return result
    }

    override suspend fun isOnlineSource(path: String): Boolean = URLUtil.isValidUrl(path)

}
