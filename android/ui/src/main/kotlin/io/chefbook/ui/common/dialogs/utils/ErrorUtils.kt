package io.chefbook.ui.common.dialogs.utils

import io.chefbook.core.android.R
import io.chefbook.libs.exceptions.LocalProfileException
import io.chefbook.libs.exceptions.ServerException

object ErrorUtils {

  fun getDialogTitleId(error: Throwable?) =
    when (error) {
      is ServerException -> R.string.common_general_server_error
      else -> R.string.common_general_unknown_error
    }

  fun getDialogDescriptionId(e: Throwable?) =
    when (e) {
      is LocalProfileException -> R.string.common_general_app_error_local_user
      is ServerException -> getServerErrorDescriptionId(e)
      else -> null
    }

  private fun getServerErrorDescriptionId(e: ServerException) =
    when {
      e.type == ServerException.INVALID_BODY -> R.string.common_general_server_error_old_version
      e.type == ServerException.BIG_FILE -> R.string.common_general_server_error_big_file
      e.type == ServerException.UNAUTHORIZED -> R.string.common_general_server_error_authorization_failed
      e.type == ServerException.INVALID_CREDENTIALS -> R.string.common_general_server_error_invalid_credentials
      e.type == ServerException.INVALID_ACTIVATION_LINK -> R.string.common_general_server_error_invalid_activation_code
      e.type == ServerException.ACCESS_DENIED -> R.string.common_general_server_error_access_denied
      e.type == ServerException.USER_BLOCKED -> R.string.common_general_server_error_profile_blocked
      e.type == ServerException.USER_EXISTS -> R.string.common_general_server_error_profile_exists
      e.type == ServerException.NOT_FOUND -> R.string.common_general_nothing_found
      else -> null
    }
}
