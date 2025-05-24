package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.models.auth.LOCAL_PROFILE_ID
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.onFailure
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import io.chefbook.sdk.auth.impl.data.sources.local.SessionsSource
import io.chefbook.sdk.auth.impl.data.sources.remote.SessionRefreshSource
import io.chefbook.sdk.network.api.internal.clients.ProfileHttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.flow.Flow

internal class SessionsRepositoryImpl(
  private val sessionsSource: SessionsSource,
  private val sessionRefreshSource: SessionRefreshSource,
  private val httpClientFactory: ProfileHttpClientFactory,
) : SessionsRepository {

  override fun observeSessions(): Flow<Map<String, Session>> =
    sessionsSource.observeSessionsInfo()

  override fun observeCurrentProfileId(): Flow<String?> =
    sessionsSource.observeCurrentProfileId()

  override suspend fun refreshTokens(): EmptyResult {
    val session = sessionsSource.getCurrentSessionInfo()
      ?: return Result.failure(NotFoundException())

    if (session.profileId == LOCAL_PROFILE_ID) return successResult

    val refreshToken = session.refreshToken
      ?: return Result.failure(NotFoundException())

    return sessionRefreshSource.refreshSession(
      httpClientFactory.baseClient,
      refreshToken,
    )
      .onSuccess { updatedSession ->
        sessionsSource.saveSession(updatedSession)
        clearClientTokens(session.profileId)
      }
      .onFailure {
        sessionsSource.endSession(session.profileId)
      }
      .asEmpty()
  }

  override fun clearClientTokens(profileId: String) {
    httpClientFactory.baseClient.clearTokens()
    httpClientFactory.get(profileId)?.clearTokens()
  }

  private fun HttpClient.clearTokens() {
    authProviders
      .filterIsInstance<BearerAuthProvider>()
      .first().clearToken()
  }
}
