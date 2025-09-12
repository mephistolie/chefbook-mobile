package io.chefbook.features.auth.signin.login.mvi

import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.libs.utils.auth.isEmail
import io.chefbook.libs.utils.auth.isNickname
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private typealias State = SignInLoginState
private typealias SideEffect = SignInLoginSideEffect
private typealias Intent = SignInLoginIntent

interface SignInLoginStore : Store<State, SideEffect, Intent>

class SignInLoginStoreImpl(
  initialLogin: String,
) : BaseStore<State, SideEffect, Intent>(), SignInLoginStore {

  override val stateFlow: StateFlow<State> field = MutableStateFlow(
    State(
      login = initialLogin,
      isSignInButtonEnabled = isLoginValid(initialLogin),
      isProfileListButtonVisible = false,
    )
  )

  override suspend fun reduce(intent: Intent) {
    when (intent) {
      SignInLoginIntent.SignedInProfilesButtonClicked -> reduceSignedInProfilesButtonClicked()
      is SignInLoginIntent.LoginEntered -> reduceLoginEntered(intent.login)
      SignInLoginIntent.SignInButtonClicked -> reduceSignInButtonClicked()
      SignInLoginIntent.SignUpButtonClicked -> reduceSignUpButtonClicked()
    }
  }

  private suspend fun reduceSignedInProfilesButtonClicked() {
    sideEffectsFlow.emit(SignInLoginSideEffect.SignedInProfilesButtonClicked)
  }

  private fun reduceLoginEntered(login: String) {
    val formattedLogin = login.trim()
    stateFlow.update { state ->
      state.copy(
        login = formattedLogin,
        isSignInButtonEnabled = isLoginValid(formattedLogin),
      )
    }
  }

  private suspend fun reduceSignInButtonClicked() {
    sideEffectsFlow.emit(SignInLoginSideEffect.SignInButtonClicked(login = stateFlow.value.login))
  }

  private suspend fun reduceSignUpButtonClicked() {
    sideEffectsFlow.emit(
      value = SignInLoginSideEffect.SignedUpProfilesButtonClicked(login = stateFlow.value.login),
    )
  }

  private fun isLoginValid(login: String): Boolean = isNickname(login) || isEmail(login)
}