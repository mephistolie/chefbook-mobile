package io.chefbook.features.auth.profiles.ui.mvi

interface ProfilesListSideEffect {

  data class ProfileSelectedButtonClicked(
    val profileId: String,
  ) : ProfilesListSideEffect

  data object AnotherProfileButtonClicked : ProfilesListSideEffect
}
