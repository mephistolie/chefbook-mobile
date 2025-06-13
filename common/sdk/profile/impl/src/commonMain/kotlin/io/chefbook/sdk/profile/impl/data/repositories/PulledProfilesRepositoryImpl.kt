package io.chefbook.sdk.profile.impl.data.repositories

import io.chefbook.libs.models.auth.LOCAL_PROFILE_ID
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfilesSource
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfilesSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

open class PulledProfilesRepositoryImpl(
  private val localSource: LocalProfilesSource,
  private val remoteSource: RemoteProfilesSource,
  private val profileScope: CoroutineScope,
) : PulledProfilesRepository {

  private val pulledProfiles = mutableSetOf<String>()
  private val mutex = Mutex()

  override fun pullProfilesAsync(profileIds: List<String>) {
    profileScope.launch {
      val profilesToPull =
        profileIds.filter { profileId -> addToPull(profileId) }
      profilesToPull.forEach(::pullProfile)
    }
  }

  override fun pullProfileAsync(profileId: String) {
    profileScope.launch {
      if (!addToPull(profileId)) return@launch
      pullProfile(profileId)
    }
  }

  private suspend fun addToPull(profileId: String): Boolean {
    if (profileId == LOCAL_PROFILE_ID) return false
    if (pulledProfiles.contains(profileId)) return false

    mutex.withLock {
      if (pulledProfiles.contains(profileId)) return false
      pulledProfiles.add(profileId)
      return true
    }
  }

  private fun pullProfile(profileId: String) {
    profileScope.launch {
      remoteSource.getProfile(profileId)
        .onSuccess { localSource.cacheProfile(it) }
        .onFailure { mutex.withLock { pulledProfiles.remove(profileId) } }
    }
  }

  override suspend fun cacheProfile(profile: Profile) {
    mutex.withLock { pulledProfiles.add(profile.id) }
    localSource.cacheProfile(profile)
  }

  override suspend fun clearProfileCache(profileId: String) {
    mutex.withLock { pulledProfiles.remove(profileId) }
    localSource.clearProfileCache(profileId)
  }
}
