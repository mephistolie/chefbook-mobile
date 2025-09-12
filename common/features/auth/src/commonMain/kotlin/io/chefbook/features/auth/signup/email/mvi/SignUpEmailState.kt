package io.chefbook.features.auth.signup.email.mvi

data class SignUpEmailState(
  val email: String = "",
  val isSignUpButtonEnabled: Boolean = false,
)
