package io.chefbook.features.auth.signup.password.mvi

sealed interface SignUpPasswordIntent {

  data object Back : SignUpPasswordIntent

  data class PasswordEntered(val password: String) : SignUpPasswordIntent

  data class PasswordValidationEntered(val passwordValidation: String) : SignUpPasswordIntent

  data object SignUpButtonClicked : SignUpPasswordIntent
}
