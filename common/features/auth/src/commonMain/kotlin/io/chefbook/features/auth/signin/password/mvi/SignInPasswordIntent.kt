package io.chefbook.features.auth.signin.password.mvi

sealed interface SignInPasswordIntent {

  data object Back : SignInPasswordIntent

  data class PasswordEntered(val password: String) : SignInPasswordIntent

  data object SignInButtonClicked : SignInPasswordIntent

  data object ResetPasswordButtonClicked : SignInPasswordIntent
}
