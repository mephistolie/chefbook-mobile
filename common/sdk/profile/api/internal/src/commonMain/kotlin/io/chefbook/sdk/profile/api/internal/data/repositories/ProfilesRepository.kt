package io.chefbook.sdk.profile.api.internal.data.repositories

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ProfilesRepository {

  fun observeProfiles(): Flow<List<Profile>>

  suspend fun getProfiles(): List<Profile>
}
