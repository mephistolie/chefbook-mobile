package com.cactusknights.chefbook.domain.entities.action

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
