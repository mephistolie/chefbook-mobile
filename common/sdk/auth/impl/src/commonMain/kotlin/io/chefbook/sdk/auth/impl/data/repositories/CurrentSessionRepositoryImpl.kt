package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository
import io.chefbook.sdk.auth.impl.data.sources.local.CurrentSessionLocalDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class CurrentSessionRepositoryImpl(
  private val authDataSource: AuthDataSource,
  private val currentSessionDataSource: CurrentSessionLocalDataSource,
) : CurrentSessionRepository {

  override fun observeSessionAlive() =
    currentSessionDataSource.observeSessionInfo()
      .map { it != null && it.profileDeletionTimestamp == null }

  override suspend fun isSessionAlive() =
    observeSessionAlive().first()

  override suspend fun finishSession() {
    currentSessionDataSource.getSessionInfo()?.refreshToken?.let { refreshToken ->
      currentSessionDataSource.clearTokens()
      authDataSource.signOut(refreshToken)
    }
  }
}
