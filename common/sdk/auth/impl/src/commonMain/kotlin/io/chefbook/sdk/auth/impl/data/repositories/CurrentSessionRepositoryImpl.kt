package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository
import io.chefbook.sdk.auth.impl.data.sources.local.TokensDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class CurrentSessionRepositoryImpl(
  private val authDataSource: AuthDataSource,
  private val tokensDataSource: TokensDataSource,
) : CurrentSessionRepository {

  override fun observeSessionAlive() =
    tokensDataSource.observeTokens().map { it != null }

  override suspend fun isSessionAlive() =
    observeSessionAlive().first()

  override suspend fun finishSession() {
    tokensDataSource.getTokens()?.refreshToken?.let { refreshToken ->
      tokensDataSource.clearTokens()
      authDataSource.signOut(refreshToken)
    }
  }
}
