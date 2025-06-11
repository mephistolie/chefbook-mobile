package io.chefbook.features.auth.profiles.ui.mvi

sealed interface ProfilesListIntent {

  data class ProfileSelectedButtonClicked(
    val profileId: String,
  ) : ProfilesListIntent

  data object AnotherProfileButtonClicked : ProfilesListIntent
}
