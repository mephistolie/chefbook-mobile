package io.chefbook.sdk.core.impl.data.repositories

import io.chefbook.sdk.auth.api.internal.data.repositories.SessionRepository
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class DataSourcesRepositoryImpl(
  private val sessionRepository: SessionRepository,
  private val connectivityRepository: ConnectivityRepository,
) : DataSourcesRepository {

  override fun observeRemoteSourceAvailability(): Flow<Boolean> = combine(
    sessionRepository.observeSessionAlive(),
    connectivityRepository.observeConnectivity()
  ) { isSessionAlive, hasConnection ->
    isSessionAlive && hasConnection
  }

  override suspend fun isRemoteSourceAvailable(): Boolean =
    observeRemoteSourceAvailability().first()

  override suspend fun isRemoteSourceEnabled(): Boolean =
    sessionRepository.isSessionOnline()
}
