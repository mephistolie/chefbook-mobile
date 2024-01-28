package io.chefbook.sdk.profile.impl.data.sources.local

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.impl.data.sources.ProfileSource
import kotlinx.coroutines.flow.Flow

interface LocalProfileSource : ProfileSource {

  fun observeProfile(): Flow<Profile>

  suspend fun updateProfileCache(update: (Profile) -> Profile)

  suspend fun cacheProfileInfo(info: Profile)

  suspend fun clearProfileCache()
}
