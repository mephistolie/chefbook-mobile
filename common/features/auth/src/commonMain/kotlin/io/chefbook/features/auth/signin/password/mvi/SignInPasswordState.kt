package io.chefbook.features.auth.signin.password.mvi

data class SignInPasswordState(
  val login: String,
  val password: String = "",
  val isSignInButtonEnabled: Boolean = false,
  val isSignInButtonLoading: Boolean = false,
)
