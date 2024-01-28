package io.chefbook.features.auth.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class AuthScreenIntent : MviIntent {
  data object OpenSignInForm : AuthScreenIntent()
  data object OpenSignUpForm : AuthScreenIntent()
  data object OpenPasswordResetScreen : AuthScreenIntent()
  data class SetEmail(val email: String) : AuthScreenIntent()
  data class SetPassword(val password: String) : AuthScreenIntent()
  data class SetPasswordValidation(val validation: String) : AuthScreenIntent()
  data object AuthButtonClicked : AuthScreenIntent()
  data object ChooseLocalMode : AuthScreenIntent()
}