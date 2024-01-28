package io.chefbook.sdk.core.api.internal.data.repositories

import kotlinx.coroutines.flow.Flow

interface DataSourcesRepository {

  fun observeRemoteSourceAvailability(): Flow<Boolean>

  suspend fun isRemoteSourceAvailable(): Boolean

  suspend fun isRemoteSourceEnabled(): Boolean
}
