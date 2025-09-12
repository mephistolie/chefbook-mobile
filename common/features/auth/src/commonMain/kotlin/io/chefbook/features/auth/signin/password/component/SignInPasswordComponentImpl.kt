package io.chefbook.features.auth.signin.password.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.auth.signin.password.mvi.SignInPasswordStore
import io.chefbook.libs.mvi.decompose.retainedStore
import org.koin.core.parameter.parametersOf

class SignInPasswordComponentImpl(
  componentContext: ComponentContext,
  login: String,
  private val onBackButtonClicked: () -> Unit,
  private val onResetPasswordClicked: () -> Unit,
) : ComponentContext by componentContext, SignInPasswordComponent {

  override val store: SignInPasswordStore by retainedStore<SignInPasswordStore>{ parametersOf(login) }

  override fun onBackButtonClicked() {
    onBackButtonClicked.invoke()
  }

  override fun onResetPasswordClicked() {
    onResetPasswordClicked.invoke()
  }
}
