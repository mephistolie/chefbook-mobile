package io.chefbook.features.auth.signin.login.mvi

data class SignInLoginState(
  val login: String = "",
  val isSignInButtonEnabled: Boolean = false,
  val isProfileListButtonVisible: Boolean = false,
)
