package com.mysty.chefbook.api.common.room

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType

internal object RoomHandler {

    suspend fun <T> handleQuery(block: suspend () -> ActionStatus<T>) =
        try {
            block()
        } catch (e: Exception) {
            Failure(AppError(AppErrorType.UNKNOWN_ERROR))
        }

}
