package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth

import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInGoogleRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignOutRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignUpRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignUpResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody

internal class AuthApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), AuthApiService {

  override suspend fun signUp(body: SignUpRequest): Result<SignUpResponse> =
    safePost("$AUTH_ROUTE/sign-up") { setBody(body) }

  override suspend fun activateProfile(userId: String, code: String): Result<MessageResponse> =
    safeGet("$AUTH_ROUTE/activate") {
      parameter("user_id", userId)
      parameter("code", code)
    }

  override suspend fun signIn(body: SignInRequest): Result<TokensResponse> =
    safePost("$AUTH_ROUTE/sign-in") { setBody(body) }

  override suspend fun signInGoogle(body: SignInGoogleRequest): Result<TokensResponse> =
    safePost("$AUTH_ROUTE/google") { setBody(body) }

  override suspend fun signOut(body: SignOutRequest): Result<MessageResponse> =
    safePost("$AUTH_ROUTE/sign-out") { setBody(body) }

  companion object {
    private const val AUTH_ROUTE = "/v1/auth"
  }
}
