package io.chefbook.sdk.auth.api.internal.data.repositories

import kotlinx.coroutines.flow.Flow

interface CurrentSessionRepository {
  fun observeSessionAlive(): Flow<Boolean>
  suspend fun isSessionAlive(): Boolean
  suspend fun finishSession()
}
