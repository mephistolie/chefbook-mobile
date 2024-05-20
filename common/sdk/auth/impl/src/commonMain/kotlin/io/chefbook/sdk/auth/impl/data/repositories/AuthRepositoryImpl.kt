package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.onFailure
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.auth.impl.data.sources.local.CurrentSessionLocalDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionRemoteDataSource
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class AuthRepositoryImpl(
  private val remoteSource: AuthDataSource,
  private val currentSessionLocalSource: CurrentSessionLocalDataSource,
  private val currentSessionRemoteSource: CurrentSessionRemoteDataSource,
  private val profileRepository: ProfileRepository,
  private val settingsRepository: SettingsRepository,
  private val localDataRepository: LocalDataRepository,
  private val client: HttpClient,
  private val scopes: CoroutineScopes,
) : AuthRepository {

  init {
    handleSessionDeath()
  }

  override suspend fun signUp(email: String, password: String) =
    remoteSource.signUp(email, password)

  override suspend fun activateProfile(userId: String, code: String) =
    remoteSource.activateProfile(userId, code)

  override suspend fun signIn(login: String, password: String) =
    remoteSource.signIn(login, password)
      .onSuccess { session ->
        currentSessionLocalSource.updateSession(session)
        refreshClientTokens()
      }
      .map { it.profileDeletionTimestamp == null }

  override suspend fun signInGoogle(idToken: String) =
    remoteSource.signInGoogle(idToken)
      .onSuccess {
        currentSessionLocalSource.updateSession(it)
        refreshClientTokens()
      }
      .map { it.profileDeletionTimestamp == null }

  private fun handleSessionDeath() {
    currentSessionLocalSource.observeSessionInfo().collectIn(scopes.repository) { session ->
      if (session != null) return@collectIn

      val profile = profileRepository.getProfile()
      if (profile.isFailure || profile.getOrNull()?.isOnline == true ||
        settingsRepository.getProfileMode() == ProfileMode.UNSPECIFIED
      ) {
        settingsRepository.setProfileMode(mode = ProfileMode.UNSPECIFIED)
        localDataRepository.clearLocalData()
        refreshClientTokens()
      }
    }
  }

  override suspend fun refreshTokens(): EmptyResult {
    val refreshToken = currentSessionLocalSource.getSessionInfo()?.refreshToken ?: return Result.failure(NotFoundException())

    return currentSessionRemoteSource.refreshSession(client, refreshToken)
      .onSuccess { session ->
        currentSessionLocalSource.updateSession(session)
        refreshClientTokens()
      }
      .onFailure {
        currentSessionLocalSource.clearTokens()
      }
      .asEmpty()
  }

  private fun refreshClientTokens() {
    client.plugin(Auth).providers
      .filterIsInstance<BearerAuthProvider>()
      .first().clearToken()
  }

  override fun observeProfileDeletionTimestamp() =
    currentSessionLocalSource.observeSessionInfo()
      .map { it?.profileDeletionTimestamp }
      .distinctUntilChanged()
}
