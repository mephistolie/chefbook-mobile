package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.time.parseInstantSafely
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.auth.impl.data.sources.local.SessionsSource
import io.chefbook.sdk.auth.impl.data.sources.remote.SessionRefreshSource
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.exceptions.InvalidRefreshTokenException
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

internal class TokensRepositoryImpl(
  private val localSource: SessionsSource,
  private val remoteSource: SessionRefreshSource,
) : TokensRepository {

  override suspend fun getTokens(): BearerTokens? =
    localSource.getCurrentSessionInfo()?.toBearerTokens()

  override suspend fun getTokens(profileId: String): BearerTokens? =
    localSource.getSessionsInfo()[profileId]?.toBearerTokens()

  override suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens? {
    with(params) {
      val refreshToken = oldTokens?.refreshToken ?: return null

      return remoteSource.refreshSession(client, refreshToken)
        .onSuccess(localSource::saveSession)
        .onFailure { e ->
          if (e is InvalidRefreshTokenException) localSource.endSessionByRefreshToken(
            refreshToken
          )
        }
        .getOrNull()
        ?.toBearerTokens()
    }
  }

  private suspend fun sendWithoutRequest(): Boolean =
    localSource.getCurrentSessionInfo().isExpired()

  private suspend fun sendWithoutRequest(profileId: String): Boolean =
    localSource.getSessionsInfo()[profileId].isExpired()

  private fun Session?.isExpired(): Boolean {
    if (this == null) return false

    val expirationTimestamp = parseInstantSafely(expirationTimestamp)
      ?: return false
    val currentTimestamp = Clock.System.now()

    return expirationTimestamp - currentTimestamp < refreshThreshold
  }

  private fun Session.toBearerTokens() =
    BearerTokens(accessToken.orEmpty(), refreshToken.orEmpty())

  private companion object {
    private val refreshThreshold = 3.minutes
  }
}
