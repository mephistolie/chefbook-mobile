package io.chefbook.features.auth.profiles.component

import io.chefbook.features.auth.profiles.mvi.ProfilesListStore

interface ProfilesListComponent {

  val store: ProfilesListStore

  fun onProfileSelected(profileId: String)

  fun onAnotherProfileButtonClicked()
}
