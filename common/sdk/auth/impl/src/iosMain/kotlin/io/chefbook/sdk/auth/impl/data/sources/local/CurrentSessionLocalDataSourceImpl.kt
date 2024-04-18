package io.chefbook.sdk.auth.impl.data.sources.local

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.coroutines.flow.Flow

internal class CurrentSessionLocalDataSourceImpl() : CurrentSessionLocalDataSource {

  override fun observeSessionInfo(): Flow<Session?> {
    TODO("Not yet implemented")
  }

  override suspend fun getSessionInfo(): Session? {
    TODO("Not yet implemented")
  }

  override suspend fun updateSession(tokens: Session) {
    TODO("Not yet implemented")
  }

  override suspend fun clearTokens() = Unit
}
