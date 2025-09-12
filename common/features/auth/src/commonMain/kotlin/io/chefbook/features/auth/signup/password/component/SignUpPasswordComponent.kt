package io.chefbook.features.auth.signup.password.component

import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordStore

interface SignUpPasswordComponent {

  val store: SignUpPasswordStore

  fun onBack()

  fun onProfileExists()

  fun onProfileBlocked()
}
