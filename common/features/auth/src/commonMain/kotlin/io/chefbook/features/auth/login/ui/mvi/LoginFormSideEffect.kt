package io.chefbook.features.auth.login.ui.mvi

interface LoginFormSideEffect {

  data class LoginFormButtonClicked(
    val login: String,
  ) : LoginFormSideEffect

  data object SignedInProfilesButtonClicked : LoginFormSideEffect
}
