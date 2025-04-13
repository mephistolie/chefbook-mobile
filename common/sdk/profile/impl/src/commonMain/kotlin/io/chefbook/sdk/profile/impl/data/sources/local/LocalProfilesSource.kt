package io.chefbook.sdk.profile.impl.data.sources.local

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface LocalProfilesSource {

  fun observeProfiles(): Flow<Map<String, Profile>>

  suspend fun cacheProfile(profile: Profile)

  suspend fun clearProfileCache(profileId: String)
}
