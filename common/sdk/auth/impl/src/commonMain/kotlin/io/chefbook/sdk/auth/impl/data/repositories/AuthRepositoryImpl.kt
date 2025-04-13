package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.libs.models.auth.LOCAL_PROFILE_ID
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import io.chefbook.sdk.auth.impl.data.sources.local.SessionsSource
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthSource

internal class AuthRepositoryImpl(
  private val authSource: AuthSource,
  private val sessionsSource: SessionsSource,
  private val sessionsRepository: SessionsRepository,
) : AuthRepository {

  override suspend fun signUp(
    email: String,
    password: String,
  ): Result<String?> =
    authSource.signUp(email, password)

  override suspend fun activateProfile(
    userId: String,
    code: String,
  ): EmptyResult =
    authSource.activateProfile(userId, code)

  override suspend fun signIn(
    login: String,
    password: String,
  ): Result<Boolean> =
    authSource.signIn(login, password)
      .onSuccess { session ->
        sessionsSource.saveSession(session, current = true)
        sessionsRepository.clearClientTokens(session.profileId)
      }
      .map { it.profileDeletionTimestamp == null }

  override suspend fun signInLocally() {
    sessionsSource.saveSession(
      session = Session(profileId = LOCAL_PROFILE_ID),
      current = true,
    )
  }

  override suspend fun signInGoogle(
    idToken: String,
  ): Result<Boolean> =
    authSource.signInGoogle(idToken)
      .onSuccess { session ->
        sessionsSource.saveSession(
          session = session,
          current = true,
        )
        sessionsRepository.clearClientTokens(session.profileId)
      }
      .map { it.profileDeletionTimestamp == null }

  override suspend fun signOut() {
    sessionsSource.setCurrentProfile(profileId = null)
  }
}
