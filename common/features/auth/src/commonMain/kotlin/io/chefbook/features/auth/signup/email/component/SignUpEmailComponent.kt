package io.chefbook.features.auth.signup.email.component

import io.chefbook.features.auth.signup.email.mvi.SignUpEmailStore

interface SignUpEmailComponent {

  val store: SignUpEmailStore

  fun onProfileListButtonClicked()

  fun onNextButtonClicked(email: String)

  fun onSignInButtonClicked(currentEmailInput: String)
}
