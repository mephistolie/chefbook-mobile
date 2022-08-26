package com.cactusknights.chefbook.domain.entities.action

data class ServerError(
    val type: ServerErrorType,
    override val message: String? = null,
) : Exception(message)

enum class ServerErrorType {
    ACCESS_DENIED,
    UNAUTHORIZED,
    INVALID_REFRESH_TOKEN,

    INVALID_BODY,
    BIG_FILE,
    NOT_FOUND,

    INVALID_CREDENTIALS,
    PROFILE_NOT_ACTIVATED,
    INVALID_ACTIVATION_LINK,
    USER_EXISTS,
    USER_BLOCKED,

    IMPORT_OLD_PROFILE_FAILED,

    INVALID_RECIPE,

    NOT_IN_RECIPE_BOOK,

    UNKNOWN_ERROR,
}
