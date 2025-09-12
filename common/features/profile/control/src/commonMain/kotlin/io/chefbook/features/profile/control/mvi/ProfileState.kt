package io.chefbook.features.profile.control.mvi

import io.chefbook.sdk.profile.api.external.domain.entities.Profile

data class ProfileState(
  val profile: Profile = Profile.local
)
