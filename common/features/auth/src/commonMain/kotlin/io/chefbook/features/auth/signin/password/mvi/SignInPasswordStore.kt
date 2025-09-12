package io.chefbook.features.auth.signin.password.mvi

import io.chefbook.libs.exceptions.ServerException
import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.ui.utils.Res
import io.chefbook.ui.utils.commonGeneralServerError
import io.chefbook.ui.utils.commonGeneralServerErrorInvalidCredentials
import io.chefbook.ui.utils.commonGeneralServerErrorProfileBlocked
import io.chefbook.ui.utils.commonGeneralServerErrorProfileNotActivated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.getString

private typealias State = SignInPasswordState
private typealias SideEffect = SignInPasswordSideEffect
private typealias Intent = SignInPasswordIntent

interface SignInPasswordStore : Store<State, SideEffect, Intent>

class SignInPasswordStoreImpl(
  login: String,
  private val signInUseCase: SignInUseCase,
) : BaseStore<State, SideEffect, Intent>(), SignInPasswordStore {

  override val stateFlow: StateFlow<State> field = MutableStateFlow(
    State(
      login = login,
    )
  )

  override suspend fun reduce(intent: Intent) {
    when (intent) {
      SignInPasswordIntent.Back -> reduceBack()
      is SignInPasswordIntent.PasswordEntered -> reducePasswordEntered(intent.password)
      SignInPasswordIntent.SignInButtonClicked -> reduceSignInButtonClicked()
      SignInPasswordIntent.ResetPasswordButtonClicked -> reduceResetPasswordButtonClicked()
    }
  }

  private suspend fun reduceBack() {
    sideEffectsFlow.emit(SignInPasswordSideEffect.Back)
  }

  private fun reducePasswordEntered(password: String) {
    stateFlow.update { state ->
      state.copy(
        password = password.trim(),
        isSignInButtonEnabled = password.isNotBlank(),
      )
    }
  }

  private suspend fun reduceSignInButtonClicked() {
    val state = stateFlow.value

    stateFlow.update { state -> state.copy(isSignInButtonLoading = true) }
    val result = signInUseCase.invoke(state.login, state.password)
    val e = result.exceptionOrNull() ?: return

    stateFlow.update { state ->
      state.copy(
        password = "",
        isSignInButtonEnabled = false,
        isSignInButtonLoading = false,
      )
    }

    if (e is ServerException) {
      when {
        e.type == ServerException.Companion.INVALID_CREDENTIALS -> {
          showToast(getString(Res.string.commonGeneralServerErrorInvalidCredentials))
        }

        e.type == ServerException.Companion.PROFILE_NOT_ACTIVATED -> {
          showToast(getString(Res.string.commonGeneralServerErrorProfileNotActivated))
          sideEffectsFlow.emit(SignInPasswordSideEffect.Back)
        }

        e.type == ServerException.Companion.PROFILE_BLOCKED -> {
          showToast(getString(Res.string.commonGeneralServerErrorProfileBlocked))
          sideEffectsFlow.emit(SignInPasswordSideEffect.Back)
        }

        e.isServerSide -> {
          showToast(getString(Res.string.commonGeneralServerError))
        }
      }
    }
  }

  private suspend fun showToast(message: String) {
    sideEffectsFlow.emit(SignInPasswordSideEffect.ToastShown(message))
  }

  private suspend fun reduceResetPasswordButtonClicked() {
    sideEffectsFlow.emit(SignInPasswordSideEffect.ResetPasswordButtonClicked)
  }
}