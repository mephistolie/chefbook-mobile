package io.chefbook.features.auth.profiles.mvi

import io.chefbook.libs.models.profile.ProfileInfo

data class ProfilesListState(
  val profiles: List<ProfileInfo> = emptyList(),
)
