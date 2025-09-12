package io.chefbook.features.auth.signin.password.mvi

sealed interface SignInPasswordSideEffect {

  data object Back : SignInPasswordSideEffect

  data object ResetPasswordButtonClicked : SignInPasswordSideEffect

  data class ToastShown(val message: String) : SignInPasswordSideEffect
}
