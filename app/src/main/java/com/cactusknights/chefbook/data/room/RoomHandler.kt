package com.cactusknights.chefbook.data.room

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.Failure

object RoomHandler {

    suspend fun <T> handleQuery(block: suspend () -> ActionStatus<T>) =
        try {
            block()
        } catch (e: Exception) {
            Failure(AppError(AppErrorType.UNKNOWN_ERROR))
        }

}
