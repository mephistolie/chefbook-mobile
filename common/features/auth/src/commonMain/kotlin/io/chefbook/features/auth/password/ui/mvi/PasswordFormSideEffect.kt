package io.chefbook.features.auth.password.ui.mvi

interface PasswordFormSideEffect {

  data class PasswordFormButtonClicked(
    val login: String,
  ) : PasswordFormSideEffect

  data object SignedInProfilesButtonClicked : PasswordFormSideEffect

  data class ToastShown(val message: String) : PasswordFormSideEffect
}
