package io.chefbook.features.auth.signup.activation.mvi

import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenProfileActivated
import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.sdk.auth.api.external.domain.usecases.ActivateProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.getString

private typealias State = ProfileActivationState
private typealias SideEffect = ProfileActivationSideEffect
private typealias Intent = ProfileActivationIntent

interface ProfileActivationStore : Store<State, SideEffect, Intent>

class ProfileActivationStoreImpl(
  private val profileId: String,
  email: String,
  private val activateProfileUseCase: ActivateProfileUseCase,
) : BaseStore<State, SideEffect, Intent>(), ProfileActivationStore {

  override val stateFlow: StateFlow<State> field = MutableStateFlow(
    State(
      email = email,
    )
  )

  override suspend fun reduce(intent: Intent) {
    when (intent) {
      ProfileActivationIntent.Back -> reduceBack()
      is ProfileActivationIntent.CodeEntered -> TODO()
    }
  }

  private suspend fun reduceBack() {
    sideEffectsFlow.emit(ProfileActivationSideEffect.Back)
  }

  private suspend fun reduceCodeEntered(code: String) {
    if (code.length >= ProfileActivationCodeLength) return

    stateFlow.update { state ->
      state.copy(
        code = code.trim(),
      )
    }

    if (code.length < ProfileActivationCodeLength) return

    activateProfileUseCase(profileId, code)
      .onSuccess {
        showToast(getString(Res.string.commonAuthScreenProfileActivated))
        sideEffectsFlow.emit(ProfileActivationSideEffect.ProfileActivated)
      }
      .onFailure {
        stateFlow.update { state -> state.copy(code = "") }
      }
  }

  private suspend fun showToast(message: String) {
    sideEffectsFlow.emit(ProfileActivationSideEffect.ToastShown(message))
  }
}