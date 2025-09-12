package io.chefbook.features.auth.signup.activation.mvi

const val ProfileActivationCodeLength = 6

data class ProfileActivationState(
  val email: String?,
  val code: String = "",
)
