package io.chefbook.features.auth.password.ui.mvi

sealed interface PasswordFormIntent {

  data class PasswordEntered(val password: String) : PasswordFormIntent

  data object SignInButtonClicked : PasswordFormIntent
}
