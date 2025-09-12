package io.chefbook.features.auth.signup.email.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.auth.signup.email.mvi.SignUpEmailIntent
import io.chefbook.features.auth.signup.email.mvi.SignUpEmailStore
import io.chefbook.libs.mvi.decompose.retainedStore
import org.koin.core.parameter.parametersOf

class SignUpEmailComponentImpl(
  componentContext: ComponentContext,
  private val onProfileListButtonClicked: () -> Unit,
  private val onNextButtonClicked: (email: String) -> Unit,
  private val onSignInButtonClicked: (login: String) -> Unit,
) : ComponentContext by componentContext, SignUpEmailComponent {

  override val store: SignUpEmailStore by retainedStore<SignUpEmailStore> { parametersOf("") }

  override fun onProfileListButtonClicked() {
    onProfileListButtonClicked.invoke()
  }

  override fun onNextButtonClicked(email: String) {
    onNextButtonClicked.invoke(email)
  }

  override fun onSignInButtonClicked(currentEmailInput: String) {
    onSignInButtonClicked.invoke(currentEmailInput)
  }

  fun setEmail(email: String) {
    store.handle(SignUpEmailIntent.EmailEntered(email))
  }
}
