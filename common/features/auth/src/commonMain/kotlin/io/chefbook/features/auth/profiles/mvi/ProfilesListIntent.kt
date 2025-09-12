package io.chefbook.features.auth.profiles.mvi

sealed interface ProfilesListIntent {

  data class SelectProfileButtonClicked(
    val profileId: String,
  ) : ProfilesListIntent

  data object AnotherProfileButtonClicked : ProfilesListIntent
}
