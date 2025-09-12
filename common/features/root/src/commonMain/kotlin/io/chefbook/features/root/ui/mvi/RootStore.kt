package io.chefbook.features.root.ui.mvi

import io.chefbook.libs.coroutines.collectIn
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveCurrentProfileIdUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private typealias State = RootState
private typealias SideEffect = RootSideEffect

interface RootStore : Store<State, SideEffect, Nothing>

class RootStoreImpl(
  private val observeSettingsUseCase: ObserveSettingsUseCase,
  private val observeCurrentProfileIdUseCase: ObserveCurrentProfileIdUseCase,
//  private val observeProfileDeletionUseCase: ObserveProfileDeletionUseCase,
) : BaseStore<State, SideEffect, Nothing>(), RootStore {

  override val stateFlow: StateFlow<RootState> field = MutableStateFlow(RootState())

  init {
    observeAppState()
  }

  private fun observeAppState() {
    observeCurrentProfileIdUseCase().collectIn(storeScope) { profileId ->
      sideEffectsFlow.emit(
        when (profileId) {
          null -> RootSideEffect.SignedOut
          else -> RootSideEffect.SignedIn(profileId = profileId)
        }
      )
    }

    observeSettingsUseCase().collectIn(storeScope) { settings ->
      stateFlow.update { state -> state.copy(theme = settings.appTheme) }
    }
  }

  override suspend fun reduce(intent: Nothing) = Unit
}