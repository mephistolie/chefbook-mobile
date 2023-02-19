package com.mysty.chefbook.api.common.network.mappers

import com.mysty.chefbook.api.common.communication.errors.ServerErrorType

internal object ServerErrorTypeMapper {
    private val errorMap = mapOf(
        "access_denied" to ServerErrorType.ACCESS_DENIED,
        "unauthorized" to ServerErrorType.UNAUTHORIZED,
        "invalid_refresh_token" to ServerErrorType.INVALID_REFRESH_TOKEN,
        "invalid_body" to ServerErrorType.INVALID_BODY,
        "big_file" to ServerErrorType.BIG_FILE,
        "not_found" to ServerErrorType.NOT_FOUND,
        "invalid_credentials" to ServerErrorType.INVALID_CREDENTIALS,
        "profile_not_activated" to ServerErrorType.PROFILE_NOT_ACTIVATED,
        "invalid_activation_link" to ServerErrorType.INVALID_ACTIVATION_LINK,
        "user_exists" to ServerErrorType.USER_EXISTS,
        "user_blocked" to ServerErrorType.USER_BLOCKED,
        "import_old_profile_failed" to ServerErrorType.IMPORT_OLD_PROFILE_FAILED,
        "invalid_recipe" to ServerErrorType.INVALID_RECIPE,
        "not_in_recipe_book" to ServerErrorType.NOT_IN_RECIPE_BOOK,
    )

    fun map(errorType: String) = errorMap[errorType.lowercase()] ?: ServerErrorType.UNKNOWN_ERROR
}
