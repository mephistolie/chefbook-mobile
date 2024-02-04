package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.auth.impl.data.sources.local.TokensDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataSource
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin

internal class AuthRepositoryImpl(
  private val remoteSource: AuthDataSource,
  private val tokensSource: TokensDataSource,
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
      .onSuccess {
        tokensSource.updateTokens(it)
        refreshClientTokens()
      }
      .asEmpty()

  override suspend fun signInGoogle(idToken: String) =
    remoteSource.signInGoogle(idToken)
      .onSuccess {
        tokensSource.updateTokens(it)
        refreshClientTokens()
      }
      .asEmpty()

  private fun handleSessionDeath() {
    tokensSource.observeTokens().collectIn(scopes.repository) { session ->
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

  private fun refreshClientTokens() {
    client.plugin(Auth).providers
      .filterIsInstance<BearerAuthProvider>()
      .first().clearToken()
  }
}
