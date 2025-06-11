package io.chefbook.features.auth.profiles.component

interface ProfilesListComponent {

  fun onProfileSelected(profileId: String)

  fun onAnotherProfileButtonClicked()
}
