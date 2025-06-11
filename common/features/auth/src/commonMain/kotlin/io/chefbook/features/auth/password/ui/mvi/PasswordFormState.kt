package io.chefbook.features.auth.password.ui.mvi

data class PasswordFormState(
  val login: String,
  val password: String = "",
  val isSignInButtonEnabled: Boolean = false,
  val isSignInButtonLoading: Boolean = false,
)
