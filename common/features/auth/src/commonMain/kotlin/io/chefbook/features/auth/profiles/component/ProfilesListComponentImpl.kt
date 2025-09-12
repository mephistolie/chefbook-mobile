package io.chefbook.features.auth.profiles.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.auth.profiles.mvi.ProfilesListStore
import io.chefbook.libs.mvi.decompose.retainedStore

class ProfilesListComponentImpl(
  componentContext: ComponentContext,
  private val onProfileSelected: (String) -> Unit,
  private val onAnotherProfileButtonClicked: () -> Unit,
) : ComponentContext by componentContext, ProfilesListComponent {

  override val store: ProfilesListStore by retainedStore()

  override fun onProfileSelected(profileId: String) {
    onProfileSelected.invoke(profileId)
  }

  override fun onAnotherProfileButtonClicked() {
    onAnotherProfileButtonClicked.invoke()
  }
}
