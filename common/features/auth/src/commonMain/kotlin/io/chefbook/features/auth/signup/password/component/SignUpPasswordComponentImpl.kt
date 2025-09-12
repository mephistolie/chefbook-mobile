package io.chefbook.features.auth.signup.password.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordStore
import io.chefbook.libs.mvi.decompose.retainedStore
import org.koin.core.parameter.parametersOf

class SignUpPasswordComponentImpl(
  componentContext: ComponentContext,
  login: String,
  private val onBackButtonClicked: () -> Unit,
  private val onProfileExists: () -> Unit,
  private val onProfileBlocked: () -> Unit,
) : ComponentContext by componentContext, SignUpPasswordComponent {

  override val store: SignUpPasswordStore by retainedStore<SignUpPasswordStore>{ parametersOf(login) }

  override fun onBack() {
    onBackButtonClicked.invoke()
  }

  override fun onProfileExists() {
    onProfileExists.invoke()
  }

  override fun onProfileBlocked() {
    onProfileBlocked.invoke()
  }
}
