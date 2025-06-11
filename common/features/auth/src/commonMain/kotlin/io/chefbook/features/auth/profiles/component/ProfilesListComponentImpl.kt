package io.chefbook.features.auth.profiles.component

import com.arkivanov.decompose.ComponentContext

class ProfilesListComponentImpl(
  componentContext: ComponentContext,
  private val onProfileSelected: (String) -> Unit,
  private val onAnotherProfileButtonClicked: () -> Unit,
) : ComponentContext by componentContext, ProfilesListComponent {

  override fun onProfileSelected(profileId: String) {
    onProfileSelected.invoke(profileId)
  }

  override fun onAnotherProfileButtonClicked() {
    onAnotherProfileButtonClicked.invoke()
  }
}
