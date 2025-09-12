package io.chefbook.features.auth.signin.login.mvi

sealed interface SignInLoginSideEffect {

  data class SignInButtonClicked(
    val login: String,
  ) : SignInLoginSideEffect

  data object SignedInProfilesButtonClicked : SignInLoginSideEffect

  data class SignedUpProfilesButtonClicked(
    val login: String,
  ) : SignInLoginSideEffect
}
