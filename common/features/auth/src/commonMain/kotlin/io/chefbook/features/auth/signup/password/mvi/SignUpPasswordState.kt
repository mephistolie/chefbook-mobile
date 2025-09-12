package io.chefbook.features.auth.signup.password.mvi

data class SignUpPasswordState(
  val email: String,
  val password: String = "",
  val passwordValidation: String = "",
  val isSignUpButtonEnabled: Boolean = false,
  val isSignUpButtonLoading: Boolean = false,
)
