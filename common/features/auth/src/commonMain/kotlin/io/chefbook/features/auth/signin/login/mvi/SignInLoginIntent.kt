package io.chefbook.features.auth.signin.login.mvi

sealed interface SignInLoginIntent {

  data object SignedInProfilesButtonClicked : SignInLoginIntent

  data class LoginEntered(val login: String) : SignInLoginIntent

  data object SignInButtonClicked : SignInLoginIntent

  data object SignUpButtonClicked : SignInLoginIntent
}
