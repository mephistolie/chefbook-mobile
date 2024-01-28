package io.chefbook.features.auth.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.libs.utils.auth.PasswordRating
import io.chefbook.libs.utils.auth.validatePassword

internal data class AuthScreenState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val passwordValidation: String = "",
    val passwordRating: PasswordRating = validatePassword(password, passwordValidation),

    val action: AuthAction = AuthAction.SIGN_IN,
    val isLoading: Boolean = false,
) : MviState

internal enum class AuthAction {
    SIGN_IN, SIGN_UP, RESET_PASSWORD
}
