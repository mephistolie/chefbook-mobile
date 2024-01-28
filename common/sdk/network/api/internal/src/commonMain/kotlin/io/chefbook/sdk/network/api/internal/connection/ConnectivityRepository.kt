package io.chefbook.sdk.network.api.internal.connection

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {

  fun observeConnectivity(): Flow<Boolean>

  suspend fun hasActiveConnection(): Boolean
}
