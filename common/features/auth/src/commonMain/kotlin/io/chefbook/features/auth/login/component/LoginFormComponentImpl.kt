package io.chefbook.features.auth.login.component

import com.arkivanov.decompose.ComponentContext

class LoginFormComponentImpl(
  componentContext: ComponentContext,
  private val onSignInButtonClicked: (String) -> Unit,
  private val onProfilesListButtonClicked: () -> Unit,
) : ComponentContext by componentContext, LoginFormComponent {

  override fun onSignInButtonClicked(login: String) {
    onSignInButtonClicked.invoke(login)
  }

  override fun onProfilesListButtonClicked() {
    onProfilesListButtonClicked.invoke()
  }
}
