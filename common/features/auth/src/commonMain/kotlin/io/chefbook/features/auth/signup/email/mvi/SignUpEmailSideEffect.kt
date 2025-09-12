package io.chefbook.features.auth.signup.email.mvi

sealed interface SignUpEmailSideEffect {

  data object SignedInProfilesButtonClicked : SignUpEmailSideEffect

  data class NextButtonClicked(
    val email: String,
  ) : SignUpEmailSideEffect

  data class SignInButtonClicked(
    val currentEmailInput: String,
  ) : SignUpEmailSideEffect
}
