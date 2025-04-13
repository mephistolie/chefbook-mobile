package io.chefbook.sdk.profile.impl.data.repositories

import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfilesRepository
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfilesSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn

class ProfilesRepositoryImpl(
  private val pulledProfilesRepository: PulledProfilesRepository,
  localSource: LocalProfilesSource,
  sessionsRepository: SessionsRepository,
  scopes: CoroutineScopes,
) : ProfilesRepository {

  private val profilesFlow = combine(
    sessionsRepository.observeSessions(),
    localSource.observeProfiles()
  ) { sessions, profilesMap ->
    val profiles = mutableListOf<Profile>()
    val profilesIds = mutableListOf<String>()

    sessions.values.sortedBy(Session::expirationTimestamp).forEach { session ->
      val profileId = session.profileId
      profilesIds.add(profileId)

      val profile = profilesMap[profileId]
      profiles.add(profile ?: Profile(id = profileId))
    }

    pulledProfilesRepository.pullProfilesAsync(profilesIds)

    return@combine profiles
  }
    .distinctUntilChanged()
    .shareIn(scopes.repository, SharingStarted.Lazily, replay = 1)

  override fun observeProfiles() = profilesFlow

  override suspend fun getProfiles() = observeProfiles().first()
}
