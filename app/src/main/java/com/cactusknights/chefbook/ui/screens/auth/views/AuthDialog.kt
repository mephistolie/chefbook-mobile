package com.cactusknights.chefbook.ui.screens.auth.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.action.NetworkError
import com.cactusknights.chefbook.domain.entities.action.ServerError
import com.cactusknights.chefbook.domain.entities.action.ServerErrorType
import com.cactusknights.chefbook.ui.views.dialogs.OneButtonDialog

@Composable
fun AuthErrorDialog(
    error: Throwable?,
    onHide: () -> Unit
) {
    OneButtonDialog(
        title = when (error) {
            is ServerError -> stringResource(id = R.string.common_auth_screen_auth_error)
            is NetworkError -> stringResource(id = R.string.common_general_network_error)
            else -> stringResource(id = R.string.common_general_unknown_error)
        },

        description = when (error) {
            is ServerError -> {
                when (error.type) {
                    ServerErrorType.INVALID_BODY, ServerErrorType.INVALID_CREDENTIALS -> stringResource(id = R.string.common_auth_screen_sign_in_invalid_credentials)
                    ServerErrorType.PROFILE_NOT_ACTIVATED -> stringResource(id = R.string.common_auth_screen_profile_not_activated)
                    ServerErrorType.USER_BLOCKED -> stringResource(id = R.string.common_auth_screen_profile_blocked)
                    ServerErrorType.USER_EXISTS -> stringResource(id = R.string.common_auth_screen_profile_exists)
                    else -> stringResource(id = R.string.common_general_unknown_error)
                }
            }
            is NetworkError -> {
                stringResource(id = R.string.common_general_network_error)
            }
            else -> stringResource(id = R.string.common_general_unknown_error)
        },
        onHide = onHide
    )
}