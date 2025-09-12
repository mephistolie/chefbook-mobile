package io.chefbook.features.auth.signin.login.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.auth.signin.login.mvi.SignInLoginIntent
import io.chefbook.features.auth.signin.login.mvi.SignInLoginStore
import io.chefbook.libs.mvi.decompose.retainedStore
import org.koin.core.parameter.parametersOf

class SignInLoginComponentImpl(
  componentContext: ComponentContext,
  private val onProfileListButtonClicked: () -> Unit,
  private val onSignInButtonClicked: (String) -> Unit,
  private val onSignUpButtonClicked: (String) -> Unit,
) : ComponentContext by componentContext, SignInLoginComponent {

  override val store: SignInLoginStore by retainedStore { parametersOf("") }

  override fun onProfileListButtonClicked() {
    onProfileListButtonClicked.invoke()
  }

  override fun onSignInButtonClicked(login: String) {
    onSignInButtonClicked.invoke(login)
  }

  override fun onSignUpButtonClicked(login: String) {
    onSignUpButtonClicked.invoke(login)
  }

  fun setLogin(login: String) {
    store.handle(SignInLoginIntent.LoginEntered(login))
  }
}
