package io.chefbook.sdk.profile.impl.data.sources

import io.chefbook.sdk.profile.api.external.domain.entities.Profile

interface ProfileSource {
  suspend fun getProfileInfo(): Result<Profile>
}
