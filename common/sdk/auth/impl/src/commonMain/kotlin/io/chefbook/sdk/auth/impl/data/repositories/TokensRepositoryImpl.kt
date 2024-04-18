package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.auth.impl.data.sources.local.CurrentSessionLocalDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionRemoteDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.expetions.InvalidRefreshTokenException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams

internal class TokensRepositoryImpl(
  private val localSource: CurrentSessionLocalDataSource,
  private val remoteSource: CurrentSessionRemoteDataSource,
) : TokensRepository {

  override suspend fun getTokens(): BearerTokens? {
    return localSource.getSessionInfo()?.toBearerTokens()
  }

  override suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens? {
    with(params) {
      val refreshToken = oldTokens?.refreshToken ?: return null

      return remoteSource.refreshSession(client, refreshToken)
        .onSuccess(localSource::updateSession)
        .onFailure { e -> if (e is InvalidRefreshTokenException) localSource.clearTokens() }
        .getOrNull()
        ?.toBearerTokens()
    }
  }

  private fun Session.toBearerTokens() =
    BearerTokens(accessToken, refreshToken)
}
