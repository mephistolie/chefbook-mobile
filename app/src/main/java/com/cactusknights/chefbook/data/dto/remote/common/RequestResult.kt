package com.cactusknights.chefbook.data.dto.remote.common

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.NetworkError

sealed class RequestResult<out T> {

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

fun <T> RequestResult<T>.toActionStatus(): ActionStatus<T> {
    return when (this) {
        is RequestResult.SuccessResponse -> ActionStatus.Success.Data(data = body)
        is RequestResult.ErrorResponse -> ActionStatus.Failure(error = body.toServerError())
        is RequestResult.NetworkError -> ActionStatus.Failure(error = NetworkError(error))
    }
}

fun <T> RequestResult<T>.isSuccess(): Boolean = this is RequestResult.SuccessResponse

fun <T> RequestResult<T>.isFailure(): Boolean = this is RequestResult.ErrorResponse || this is RequestResult.NetworkError

fun <T> RequestResult<T>.body(): T = (this as RequestResult.SuccessResponse<T>).body
