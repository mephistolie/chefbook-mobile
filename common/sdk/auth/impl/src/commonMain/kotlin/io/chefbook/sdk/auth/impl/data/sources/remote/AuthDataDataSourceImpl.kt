package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.withCast
import io.chefbook.libs.utils.auth.isEmail
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInGoogleRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignOutRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignUpRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.toBearerTokens
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.PasswordApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ChangePasswordRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.RequestPasswordResetRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ResetPasswordRequest
import io.ktor.client.plugins.auth.providers.BearerTokens

internal class AuthDataDataSourceImpl(
  private val authApi: AuthApiService,
  private val passwordApi: PasswordApiService,
) : AuthDataSource {

  override suspend fun signUp(email: String, password: String) =
    authApi.signUp(
      SignUpRequest(
        email = email,
        password = password,
        userId = generateUUID()
      )
    ).map { if (it.activated) null else it.userId }

  override suspend fun activateProfile(userId: String, code: String) =
    authApi.activateProfile(userId, code).asEmpty()

  override suspend fun signIn(login: String, password: String): Result<BearerTokens> {
    val isEmail = isEmail(login)
    val email = if (isEmail) login else null
    val nickname = if (isEmail) null else login

    val result =
      authApi.signIn(SignInRequest(email = email, nickname = nickname, password = password))
    return result.withCast(TokensResponse::toBearerTokens)
  }

  override suspend fun signInGoogle(idToken: String) =
    authApi.signInGoogle(SignInGoogleRequest(idToken = idToken))
      .withCast(TokensResponse::toBearerTokens)

  override suspend fun signOut(refreshToken: String) =
    authApi.signOut(SignOutRequest(refreshToken)).asEmpty()

  override suspend fun requestPasswordReset(login: String): EmptyResult {
    val isEmail = isEmail(login)
    val email = if (isEmail) login else null
    val nickname = if (isEmail) null else login

    return passwordApi.requestPasswordReset(
      RequestPasswordResetRequest(
        email = email,
        nickname = nickname,
      )
    ).asEmpty()
  }

  override suspend fun resetPassword(userId: String, code: String, newPassword: String) =
    passwordApi.resetPassword(
      ResetPasswordRequest(
        userId = userId,
        resetCode = code,
        newPassword = newPassword,
      )
    ).asEmpty()

  override suspend fun changePassword(oldPassword: String, newPassword: String) =
    passwordApi.changePassword(
      ChangePasswordRequest(
        oldPassword = oldPassword,
        newPassword = newPassword,
      )
    ).asEmpty()
}
