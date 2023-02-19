package com.mysty.chefbook.api.common.network

import com.mysty.chefbook.api.auth.domain.usecases.IRefreshSessionUseCase
import com.mysty.chefbook.api.common.communication.errors.ServerErrorType
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.network.dto.RequestResult
import com.mysty.chefbook.api.common.network.dto.responses.ErrorResponseBody
import com.mysty.chefbook.api.common.network.dto.responses.toServerError
import com.mysty.chefbook.api.sources.domain.usecases.ISwitchDataSourceUseCase

internal interface INetworkHandler {
    suspend operator fun <T> invoke(block: suspend () -> RequestResult<T>): RequestResult<T>
}

internal class NetworkHandler(
    private val switchDataSource: ISwitchDataSourceUseCase,
    private val refreshSession: IRefreshSessionUseCase,
) : INetworkHandler {

    override suspend operator fun <T> invoke(block: suspend () -> RequestResult<T>): RequestResult<T> {
        val initialResult = block()
        return processResult(block, initialResult)
    }

    private suspend fun <T> processResult(block: suspend () -> RequestResult<T>, result: RequestResult<T>): RequestResult<T> {
        val isResultRelevant = when (result) {
            is RequestResult.SuccessResponse -> true
            is RequestResult.ErrorResponse -> acceptErrorResponse(result.body)
            is RequestResult.NetworkError -> acceptNetworkError()
        }

        return if (isResultRelevant) result else block()
    }

    private suspend fun acceptErrorResponse(body: ErrorResponseBody): Boolean {
        val error = body.toServerError()
        return if (error.type == ServerErrorType.UNAUTHORIZED) !refreshSession().isSuccess() else true
    }

    private suspend fun acceptNetworkError() : Boolean {
        switchDataSource(useRemoteSource = false)
        return true
    }

}
