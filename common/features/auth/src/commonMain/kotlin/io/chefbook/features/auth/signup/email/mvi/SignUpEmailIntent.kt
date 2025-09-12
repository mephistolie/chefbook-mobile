package io.chefbook.features.auth.signup.email.mvi

sealed interface SignUpEmailIntent {

  data object SignedInProfilesButtonClicked : SignUpEmailIntent

  data class EmailEntered(val email: String) : SignUpEmailIntent

  data object NextButtonClicked : SignUpEmailIntent

  data object SignInButtonClicked : SignUpEmailIntent
}
