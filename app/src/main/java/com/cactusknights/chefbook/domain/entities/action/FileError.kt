package com.cactusknights.chefbook.domain.entities.action

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
