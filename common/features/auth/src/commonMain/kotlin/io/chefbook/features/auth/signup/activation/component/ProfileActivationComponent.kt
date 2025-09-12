package io.chefbook.features.auth.signup.activation.component

import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationStore

interface ProfileActivationComponent {

  val store: ProfileActivationStore

  fun onBackButtonClicked()

  fun onProfileActivated()
}
