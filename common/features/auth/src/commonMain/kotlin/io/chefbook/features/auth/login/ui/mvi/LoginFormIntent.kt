package io.chefbook.features.auth.login.ui.mvi

sealed interface LoginFormIntent {

  data class LoginFormEntered(val login: String) : LoginFormIntent

  data object LoginFormButtonClicked : LoginFormIntent

  data object SignedInProfilesButtonClicked : LoginFormIntent
}
