package io.chefbook.features.auth.signup.activation.mvi

sealed interface ProfileActivationSideEffect {

  data object Back : ProfileActivationSideEffect

  data object ProfileActivated : ProfileActivationSideEffect

  data class ToastShown(val message: String) : ProfileActivationSideEffect
}
