package io.chefbook.features.auth.signup.password.mvi

sealed interface SignUpPasswordSideEffect {

  data object Back : SignUpPasswordSideEffect

  data object ProfileExists : SignUpPasswordSideEffect

  data object ProfileBlocked : SignUpPasswordSideEffect

  data class ToastShown(val message: String) : SignUpPasswordSideEffect
}
