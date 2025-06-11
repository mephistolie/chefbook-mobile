package io.chefbook.features.auth.login.ui.mvi

data class LoginFormState(
  val login: String = "",
  val isSignInButtonEnabled: Boolean = false,
  val isProfilesListButtonVisible: Boolean = false,
)
