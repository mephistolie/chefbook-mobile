package io.chefbook.features.auth.signin.login.component

import io.chefbook.features.auth.signin.login.mvi.SignInLoginStore

interface SignInLoginComponent {

  val store: SignInLoginStore

  fun onProfileListButtonClicked()

  fun onSignInButtonClicked(login: String)

  fun onSignUpButtonClicked(login: String)
}
