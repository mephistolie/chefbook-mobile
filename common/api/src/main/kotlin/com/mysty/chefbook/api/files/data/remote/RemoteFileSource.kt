package com.mysty.chefbook.api.files.data.remote

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.errors.ServerError
import com.mysty.chefbook.api.common.communication.errors.ServerErrorType
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.files.data.IFileSource
import com.mysty.chefbook.api.files.data.remote.api.FileApi

internal class RemoteFileSource(
    private val api: FileApi,
) : IFileSource {

    override suspend fun getFile(path: String): ActionStatus<ByteArray> {
        val result = api.getFile(path)
        if (result.isFailure()) return ActionStatus.Failure(ServerError(ServerErrorType.NOT_FOUND))

        return DataResult(result.body().byteStream().readBytes())
    }

}
