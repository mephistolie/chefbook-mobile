package io.chefbook.features.auth.signup.activation.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationStore
import io.chefbook.libs.mvi.decompose.retainedStore
import org.koin.core.parameter.parametersOf

class ProfileActivationComponentImpl(
  componentContext: ComponentContext,
  profileId: String,
  email: String,
  private val onBackButtonClicked: () -> Unit,
  private val onProfileActivated: () -> Unit,
) : ComponentContext by componentContext, ProfileActivationComponent {

  override val store: ProfileActivationStore by retainedStore<ProfileActivationStore>{ parametersOf(profileId, email) }

  override fun onBackButtonClicked() {
    onBackButtonClicked.invoke()
  }

  override fun onProfileActivated() {
    onProfileActivated.invoke()
  }
}
