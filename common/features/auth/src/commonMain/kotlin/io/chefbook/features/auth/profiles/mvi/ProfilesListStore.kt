package io.chefbook.features.auth.profiles.mvi

import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private typealias State = ProfilesListState
private typealias SideEffect = ProfilesListSideEffect
private typealias Intent = ProfilesListIntent

interface ProfilesListStore : Store<State, SideEffect, Intent>

class ProfilesListStoreImpl() : BaseStore<State, SideEffect, Intent>(), ProfilesListStore {

  override val stateFlow: StateFlow<State> field = MutableStateFlow(
    State(
      profiles = listOf(
        ProfileInfo(
          id = "1",
          avatar = "https://sun9-17.userapi.com/s/v1/if1/-xsdRlUD-9URd1NpuoqKYcyowoJFKKPdBAy7Z1YKb1QHoioT_jsFwjoLZNFZa9eSf7uvJA.jpg?quality=96&crop=208,74,384,384&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360&ava=1&cs=200x200",
          name = "Уолтер Уайт",
        ),
        ProfileInfo(
          id = "2",
          avatar = "https://avatars.mds.yandex.net/i?id=d8c50d9c0d31bd565240a06217903f3b_l-8209451-images-thumbs&n=13",
          name = "Джимми МакГилл",
        )
      )
    ))

  override suspend fun reduce(intent: Intent) {
    when (intent) {
      is ProfilesListIntent.SelectProfileButtonClicked ->
        reduceProfileSelectedButtonClicked(intent.profileId)

      ProfilesListIntent.AnotherProfileButtonClicked -> reduceAnotherProfileButtonClicked()
    }
  }

  private suspend fun reduceProfileSelectedButtonClicked(profileId: String) {
    sideEffectsFlow.emit(ProfilesListSideEffect.ProfileSelected(profileId))
  }

  private suspend fun reduceAnotherProfileButtonClicked() {
    sideEffectsFlow.emit(ProfilesListSideEffect.AnotherProfileButtonClicked)
  }
}
