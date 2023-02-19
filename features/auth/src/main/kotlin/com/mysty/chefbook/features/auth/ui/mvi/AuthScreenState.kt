package com.mysty.chefbook.features.auth.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviState
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.features.auth.utils.AuthUtils
import com.mysty.chefbook.features.auth.utils.PasswordRating

internal data class AuthScreenState(
    val email: String = Strings.EMPTY,
    val isEmailValid: Boolean = false,
    val password: String = Strings.EMPTY,
    val passwordValidation: String = Strings.EMPTY,
    val passwordRating: PasswordRating = AuthUtils.validatePassword(password, passwordValidation),

    val action: AuthAction = AuthAction.SIGN_IN,
    val isLoading: Boolean = false,
) : MviState

internal enum class AuthAction {
    SIGN_IN, SIGN_UP, RESET_PASSWORD
}
