package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.libs.utils.auth.isEmail
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInGoogleRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignOutRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignUpRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.toBearerTokens
import io.ktor.client.plugins.auth.providers.BearerTokens

internal class AuthDataDataSourceImpl(
  private val api: AuthApiService,
) : AuthDataSource {

  override suspend fun signUp(email: String, password: String) =
    api.signUp(
      SignUpRequest(
        email = email,
        password = password,
        userId = generateUUID()
      )
    ).map { if (it.activated) null else it.userId }

  override suspend fun activateProfile(userId: String, code: String) =
    api.activateProfile(userId, code).asEmpty()

  override suspend fun signIn(login: String, password: String): Result<BearerTokens> {
    val isEmail = isEmail(login)
    val email = if (isEmail) login else null
    val nickname = if (isEmail) null else login

    val result =
      api.signIn(SignInRequest(email = email, nickname = nickname, password = password))
    return result.map(TokensResponse::toBearerTokens)
  }

  override suspend fun signInGoogle(idToken: String) =
    api.signInGoogle(SignInGoogleRequest(idToken = idToken))
      .map(TokensResponse::toBearerTokens)

  override suspend fun signOut(refreshToken: String) =
    api.signOut(SignOutRequest(refreshToken)).asEmpty()
}
