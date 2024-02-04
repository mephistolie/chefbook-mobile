package io.chefbook.features.profile.control.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.profile.api.external.domain.entities.Profile

internal data class ProfileScreenState(
  val profile: Profile = Profile(id = Profile.LOCAL_PROFILE_ID)
) : MviState
