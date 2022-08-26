package com.cactusknights.chefbook.data.repositories

import android.webkit.URLUtil
import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import javax.inject.Inject
import javax.inject.Singleton

interface IFileRepo {
    suspend fun getFile(path: String) : ActionStatus<ByteArray>
}

@Singleton
class FileRepo @Inject constructor(
    @Local
    private val local: IFileSource,
    @Remote
    private val remote: IFileSource,
) : IFileRepo {

    override suspend fun getFile(path: String) : ActionStatus<ByteArray> =
        if (URLUtil.isValidUrl(path)) {
            remote.getFile(path)
        } else {
            local.getFile(path)
        }

}
