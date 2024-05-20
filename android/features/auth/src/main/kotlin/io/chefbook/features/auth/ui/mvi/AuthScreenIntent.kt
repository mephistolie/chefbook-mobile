package io.chefbook.features.auth.ui.mvi

import android.content.Context
import io.chefbook.libs.mvi.MviIntent

internal sealed interface AuthScreenIntent : MviIntent {

  data class SetLogin(val login: String) : AuthScreenIntent
  data class SetPassword(val password: String) : AuthScreenIntent
  data class SetPasswordValidation(val validation: String) : AuthScreenIntent
  data class SetActivationCode(val code: String) : AuthScreenIntent

  data object OpenSignUpForm : AuthScreenIntent
  data object OpenSignUpPasswordForm : AuthScreenIntent
  data object SignUp : AuthScreenIntent

  data object OpenSignInForm : AuthScreenIntent
  data object OpenSignInPasswordForm : AuthScreenIntent
  data object SignIn : AuthScreenIntent

  data class SignInGoogleClicked(val context: Context) : AuthScreenIntent
  data object ChooseLocalMode : AuthScreenIntent

  data object RequestPasswordReset : AuthScreenIntent
  data object ConfirmPasswordReset : AuthScreenIntent

  data object RestoreProfile : AuthScreenIntent
  data object OpenSignOutConfirmationScreen : AuthScreenIntent
  data object SignOut : AuthScreenIntent
}
