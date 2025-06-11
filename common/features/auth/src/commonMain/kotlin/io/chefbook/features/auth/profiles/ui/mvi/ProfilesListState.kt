package io.chefbook.features.auth.profiles.ui.mvi

import io.chefbook.libs.models.profile.ProfileInfo

data class ProfilesListState(
  val profiles: List<ProfileInfo> = emptyList(),
)
