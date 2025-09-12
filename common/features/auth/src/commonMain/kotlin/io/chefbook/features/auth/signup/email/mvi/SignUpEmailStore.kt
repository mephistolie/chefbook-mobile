package io.chefbook.features.auth.signup.email.mvi

import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.libs.utils.auth.isEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private typealias State = SignUpEmailState
private typealias SideEffect = SignUpEmailSideEffect
private typealias Intent = SignUpEmailIntent

interface SignUpEmailStore : Store<State, SideEffect, Intent>

class SignUpEmailStoreImpl(
  initialEmail: String,
) : BaseStore<State, SideEffect, Intent>(), SignUpEmailStore {

  override val stateFlow: StateFlow<State> field = MutableStateFlow(
    SignUpEmailState(
      email = initialEmail,
      isSignUpButtonEnabled = isEmail(initialEmail),
    )
  )

  override suspend fun reduce(intent: Intent) {
    when (intent) {
      SignUpEmailIntent.SignedInProfilesButtonClicked -> reduceSignedInProfilesButtonClicked()
      is SignUpEmailIntent.EmailEntered -> reduceEmailEntered(intent.email)
      SignUpEmailIntent.NextButtonClicked -> reduceNextButtonClicked()
      SignUpEmailIntent.SignInButtonClicked -> reduceSignInButtonClicked()
    }
  }

  private suspend fun reduceSignedInProfilesButtonClicked() {
    sideEffectsFlow.emit(SignUpEmailSideEffect.SignedInProfilesButtonClicked)
  }

  private fun reduceEmailEntered(email: String) {
    val formattedEmail = email.trim()
    stateFlow.update { state ->
      state.copy(
        email = formattedEmail,
        isSignUpButtonEnabled = isEmail(email),
      )
    }
  }

  private suspend fun reduceNextButtonClicked() {
    sideEffectsFlow.emit(
      value = SignUpEmailSideEffect.NextButtonClicked(email = stateFlow.value.email),
    )
  }

  private suspend fun reduceSignInButtonClicked() {
    sideEffectsFlow.emit(SignUpEmailSideEffect.SignInButtonClicked(currentEmailInput = stateFlow.value.email))
  }
}
