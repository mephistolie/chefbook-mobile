package com.mysty.chefbook.api.common.network.dto

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.errors.NetworkError
import com.mysty.chefbook.api.common.network.dto.responses.ErrorResponseBody
import com.mysty.chefbook.api.common.network.dto.responses.toServerError

internal sealed class RequestResult<out T> {

    data class SuccessResponse<T>(
        val body: T
    ) : RequestResult<T>()

    data class ErrorResponse(
        val body: ErrorResponseBody
    ) : RequestResult<Nothing>()

    data class NetworkError(
        val error: Throwable
    ) : RequestResult<Nothing>()

}

internal fun <T> RequestResult<T>.toActionStatus(): ActionStatus<T> {
    return when (this) {
        is RequestResult.SuccessResponse -> ActionStatus.Success.Data(data = body)
        is RequestResult.ErrorResponse -> ActionStatus.Failure(error = body.toServerError())
        is RequestResult.NetworkError -> ActionStatus.Failure(error = NetworkError(error))
    }
}

internal fun <T> RequestResult<T>.isSuccess(): Boolean = this is RequestResult.SuccessResponse

internal fun <T> RequestResult<T>.isFailure(): Boolean = this is RequestResult.ErrorResponse || this is RequestResult.NetworkError

internal fun <T> RequestResult<T>.body(): T = (this as RequestResult.SuccessResponse<T>).body
