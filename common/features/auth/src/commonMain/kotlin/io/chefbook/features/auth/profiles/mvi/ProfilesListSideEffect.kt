package io.chefbook.features.auth.profiles.mvi

sealed interface ProfilesListSideEffect {

  data class ProfileSelected(
    val profileId: String,
  ) : ProfilesListSideEffect

  data object AnotherProfileButtonClicked : ProfilesListSideEffect
}
