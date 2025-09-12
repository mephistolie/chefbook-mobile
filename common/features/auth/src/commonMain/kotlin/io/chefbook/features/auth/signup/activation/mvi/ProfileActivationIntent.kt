package io.chefbook.features.auth.signup.activation.mvi

sealed interface ProfileActivationIntent {

  data object Back : ProfileActivationIntent

  data class CodeEntered(val code: String) : ProfileActivationIntent
}
