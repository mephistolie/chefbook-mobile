package io.chefbook.sdk.profile.impl.data.sources.local

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.impl.data.sources.common.dto.toSerializable
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.ProfilesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LocalProfilesSourceImpl(
  private val dataStore: ProfilesDataStore,
) : LocalProfilesSource {

  override fun observeProfiles(): Flow<Map<String, Profile>> =
    dataStore.data.map { profiles ->
      profiles.mapValues { (_, profile) -> profile.toEntity() }
    }

  override suspend fun cacheProfile(profile: Profile) {
    dataStore.updateData { profiles ->
      profiles + (profile.id to profile.toSerializable())
    }
  }

  override suspend fun clearProfileCache(profileId: String) {
    dataStore.updateData { profiles ->
      profiles.filter { (id, _) -> id != profileId }
    }
  }
}
