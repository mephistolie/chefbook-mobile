package com.mysty.chefbook.api.common.communication.errors

data class FileError(
    val type: FileErrorType,
    override val message: String? = null
) : Exception(message)

enum class FileErrorType {
    NOT_EXISTS,
    BIG_FILE,
    UNABLE_MODIFY,
    UNKNOWN_ERROR,
}
