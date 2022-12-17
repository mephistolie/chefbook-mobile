package com.mysty.chefbook.api.common.communication.errors

data class AppError(
    val type: AppErrorType,
    override val message: String? = null
) : Exception(message)

enum class AppErrorType {
    LOCAL_USER,
    NOT_FOUND,
    UNABLE_DECRYPT,
    STORAGE_LOCKED,
    UNKNOWN_ERROR,
    ALREADY_EXISTS,
}
