package com.cactusknights.chefbook.data.datasources.remote

import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.network.api.FileApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.ServerError
import com.cactusknights.chefbook.domain.entities.action.ServerErrorType
import javax.inject.Inject

class RemoteFileSource @Inject constructor(
    private val api: FileApi,
) : IFileSource {

    override suspend fun getFile(path: String): ActionStatus<ByteArray> {
        val result = api.getFile(path)
        if (result.isFailure()) return ActionStatus.Failure(ServerError(ServerErrorType.NOT_FOUND))

        return DataResult(result.body().byteStream().readBytes())
    }

}
