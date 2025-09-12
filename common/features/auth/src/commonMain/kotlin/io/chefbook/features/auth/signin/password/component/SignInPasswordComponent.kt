package io.chefbook.features.auth.signin.password.component

import io.chefbook.features.auth.signin.password.mvi.SignInPasswordStore

interface SignInPasswordComponent {

  val store: SignInPasswordStore

  fun onBackButtonClicked()

  fun onResetPasswordClicked()
}
