package io.chefbook.features.auth.password.component

import com.arkivanov.decompose.ComponentContext

class PasswordComponentImpl(
  componentContext: ComponentContext,
  private val onBackButtonClicked: () -> Unit,
) : ComponentContext by componentContext, PasswordComponent {

  override fun onBackButtonClicked() {
    onBackButtonClicked.invoke()
  }
}
