package io.chefbook.features.profile.control.mvi

import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

private const val GOOGLE_PLAY_PAGE = "https://play.google.com/store/apps/details?id=com.cactusknights.chefbook"

private typealias State = ProfileState
private typealias SideEffect = ProfileIntent
private typealias Intent = ProfileEffect

interface ProfileStore : Store<State, Intent, SideEffect>

class ProfileStoreImpl(
  observeProfileUseCase: ObserveProfileUseCase,
  private val signOutUseCase: SignOutUseCase,
) : BaseStore<State, Intent, SideEffect>(), ProfileStore {

  override val stateFlow: MutableStateFlow<State> = MutableStateFlow(State())

  init {
    storeScope.launch {
      observeProfileUseCase()
        .filterNotNull()
        .collect { profile ->
          stateFlow.value = ProfileState(profile = profile)
        }
    }
  }

  override suspend fun reduce(intent: ProfileIntent) {
    when (intent) {
      is ProfileIntent.Back -> sideEffectsFlow.emit(ProfileEffect.Back)
      is ProfileIntent.RequestLogout -> sideEffectsFlow.emit(ProfileEffect.RequestLogout)
      is ProfileIntent.SignOut -> signOutUseCase()
      is ProfileIntent.OpenProfileEditingScreen -> sideEffectsFlow.emit(ProfileEffect.ProfileEditingScreenOpened)
      is ProfileIntent.OpenAppSettingsScreen -> sideEffectsFlow.emit(ProfileEffect.AppSettingsScreenOpen)
      is ProfileIntent.RateApp -> sideEffectsFlow.emit(ProfileEffect.UrlOpened(GOOGLE_PLAY_PAGE))
      is ProfileIntent.OpenAboutAppScreen -> sideEffectsFlow.emit(ProfileEffect.AboutAppScreenOpened)
    }
  }
}
