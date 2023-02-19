package com.mysty.chefbook.ui.common.dialogs.utils

import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.communication.errors.FileError
import com.mysty.chefbook.api.common.communication.errors.NetworkError
import com.mysty.chefbook.api.common.communication.errors.ServerError
import com.mysty.chefbook.api.common.communication.errors.ServerErrorType
import com.mysty.chefbook.ui.common.R

object ErrorUtils {
    fun getDialogTitleId(error: Throwable?) =
        when (error) {
            is AppError, is FileError -> R.string.common_general_app_error
            is ServerError -> R.string.common_general_server_error
            is NetworkError -> R.string.common_general_network_error
            else -> R.string.common_general_unknown_error
        }

    fun getDialogDescriptionId(error: Throwable?) =
        when (error) {
            is AppError -> getAppError(error = error.type)
            is ServerError -> getServerErrorDescriptionId(error = error.type)
            else -> null
        }

    private fun getAppError(error: AppErrorType) =
        when (error) {
            AppErrorType.LOCAL_USER -> R.string.common_general_app_error_local_user
            AppErrorType.UNABLE_DECRYPT -> R.string.common_general_app_error_decryption_failed
            AppErrorType.ENCRYPTED_VAULT_LOCKED -> R.string.common_general_app_error_encrypted_vault_locked
            else -> null
        }

    private fun getServerErrorDescriptionId(error: ServerErrorType) =
        when (error) {
            ServerErrorType.INVALID_BODY -> R.string.common_general_server_error_old_version
            ServerErrorType.INVALID_CREDENTIALS -> R.string.common_general_server_error_invalid_credentials
            ServerErrorType.PROFILE_NOT_ACTIVATED -> R.string.common_general_server_error_invalid_credentials
            ServerErrorType.USER_BLOCKED -> R.string.common_general_server_error_profile_blocked
            ServerErrorType.USER_EXISTS -> R.string.common_general_server_error_profile_exists
            ServerErrorType.ACCESS_DENIED -> R.string.common_general_server_error_access_denied
            ServerErrorType.UNAUTHORIZED -> R.string.common_general_server_error_authorization_failed
            ServerErrorType.INVALID_REFRESH_TOKEN -> R.string.common_general_server_error_authorization_failed
            ServerErrorType.BIG_FILE -> R.string.common_general_server_error_big_file
            ServerErrorType.NOT_FOUND -> R.string.common_general_server_error_old_version
            ServerErrorType.INVALID_ACTIVATION_LINK -> R.string.common_general_server_error_invalid_activation_code
            ServerErrorType.IMPORT_OLD_PROFILE_FAILED -> R.string.common_general_server_error_authorization_failed
            ServerErrorType.INVALID_RECIPE -> R.string.common_general_server_error_invalid_recipe
            ServerErrorType.NOT_IN_RECIPE_BOOK -> R.string.common_general_server_error_not_in_recipe_book
            ServerErrorType.UNKNOWN_ERROR -> R.string.common_general_unknown_error
        }
}
