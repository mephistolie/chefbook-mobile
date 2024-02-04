package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.auth.impl.data.sources.local.TokensDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.expetions.InvalidRefreshTokenException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams

internal class TokensRepositoryImpl(
  private val tokensDataSource: TokensDataSource,
  private val currentSessionDataSource: CurrentSessionDataSource,
) : TokensRepository {

  override suspend fun getTokens(): BearerTokens? {
    return tokensDataSource.getTokens()
  }

  override suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens? {
    with(params) {
      val refreshToken = oldTokens?.refreshToken ?: return null

      return currentSessionDataSource.refreshSession(client, refreshToken)
        .onSuccess(tokensDataSource::updateTokens)
        .onFailure { e -> if (e is InvalidRefreshTokenException) tokensDataSource.clearTokens() }
        .getOrNull()
    }
  }
}
