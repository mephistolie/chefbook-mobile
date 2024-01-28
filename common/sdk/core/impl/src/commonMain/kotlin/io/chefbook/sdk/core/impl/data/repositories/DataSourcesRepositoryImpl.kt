package io.chefbook.sdk.core.impl.data.repositories

import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import io.chefbook.sdk.settings.api.internal.data.repositories.ProfileModeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class DataSourcesRepositoryImpl(
  private val profileModeRepository: ProfileModeRepository,
  private val sessionRepository: CurrentSessionRepository,
  private val connectivityRepository: ConnectivityRepository,
) : DataSourcesRepository {

  override fun observeRemoteSourceAvailability(): Flow<Boolean> = combine(
    profileModeRepository.observeProfileOnlineMode(),
    sessionRepository.observeSessionAlive(),
    connectivityRepository.observeConnectivity()
  ) { isProfileOnline, isSessionAlive, hasConnection ->
    isProfileOnline && isSessionAlive && hasConnection
  }

  override suspend fun isRemoteSourceAvailable() =
    observeRemoteSourceAvailability().first()

  override suspend fun isRemoteSourceEnabled() =
    profileModeRepository.isProfileModeOnline()
}
