//package io.chefbook.features.auth.profiles.ui
//
//import io.chefbook.features.auth.profiles.ui.mvi.ProfilesListIntent
//import io.chefbook.features.auth.profiles.ui.mvi.ProfilesListSideEffect
//import io.chefbook.features.auth.profiles.ui.mvi.ProfilesListState
//import io.chefbook.libs.mvi.BaseStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//private typealias State = ProfilesListState
//private typealias SideEffect = ProfilesListSideEffect
//private typealias Intent = ProfilesListIntent
//
//class ProfilesListStore() :
//  BaseStore<State, SideEffect, Intent>() {
//
//  override val stateFlow: StateFlow<State> field = MutableStateFlow(State())
//
//  override val sideEffectsFlow: Flow<SideEffect> field = MutableSharedFlow()
//
//  override suspend fun reduce(intent: Intent) {
//    when (intent) {
//      is ProfilesListIntent.ProfileSelectedButtonClicked ->
//        reduceProfileSelectedButtonClicked(intent.profileId)
//
//      ProfilesListIntent.AnotherProfileButtonClicked -> reduceAnotherProfileButtonClicked()
//    }
//  }
//
//  private suspend fun reduceProfileSelectedButtonClicked(profileId: String) {
//    sideEffectsFlow.emit(ProfilesListSideEffect.ProfileSelectedButtonClicked(profileId))
//  }
//
//  private suspend fun reduceAnotherProfileButtonClicked() {
//    sideEffectsFlow.emit(ProfilesListSideEffect.AnotherProfileButtonClicked)
//  }
//}
