package io.chefbook.sdk.profile.impl.data.repositories

import io.chefbook.sdk.profile.api.external.domain.entities.Profile

interface PulledProfilesRepository {

  fun pullProfilesAsync(profileIds: List<String>)

  fun pullProfileAsync(profileId: String)

  suspend fun cacheProfile(profile: Profile)

  suspend fun clearProfileCache(profileId: String)
}
