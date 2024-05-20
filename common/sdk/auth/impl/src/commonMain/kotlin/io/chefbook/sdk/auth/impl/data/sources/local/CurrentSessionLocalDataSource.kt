package io.chefbook.sdk.auth.impl.data.sources.local

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.coroutines.flow.Flow

internal interface CurrentSessionLocalDataSource {

  fun observeSessionInfo(): Flow<Session?>

  suspend fun getSessionInfo(): Session?

  suspend fun updateSession(tokens: Session)

  suspend fun clearTokens()
}
