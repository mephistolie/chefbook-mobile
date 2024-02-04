package io.chefbook.features.auth.ui.mvi

import io.chefbook.libs.mvi.MviState

internal sealed interface AuthScreenState : MviState {

  data object Loading : AuthScreenState

  data class SignIn(
    val login: String = "",
    val isAuthButtonEnabled: Boolean = false,
  ) : AuthScreenState

  data class SignInPassword(
    val login: String,
    val password: String = "",
  ) : AuthScreenState

  data class PasswordReset(
    val login: String?,
  ) : AuthScreenState

  data class PasswordResetConfirmation(
    val login: String?,
    val password: String = "",
    val passwordValidation: String = "",
    val isAuthButtonEnabled: Boolean = false,
  ) : AuthScreenState

  data class SignUp(
    val email: String = "",
    val isAuthButtonEnabled: Boolean = false,
  ) : AuthScreenState

  data class SignUpPassword(
    val email: String,
    val password: String = "",
    val passwordValidation: String = "",
    val isAuthButtonEnabled: Boolean = false,
  ) : AuthScreenState

  data class ProfileActivation(
    val email: String? = null,
    val code: String,
  ) : AuthScreenState {

    companion object {
      const val CODE_LENGTH = 6
    }
  }
}
