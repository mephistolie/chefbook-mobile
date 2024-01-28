package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth

import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignInRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignOutRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignUpRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.SignUpResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface AuthApiService {

  suspend fun signUp(body: SignUpRequest): Result<SignUpResponse>

  suspend fun activateProfile(userId: String, code: String): Result<MessageResponse>

  suspend fun signIn(body: SignInRequest): Result<TokensResponse>

  suspend fun signOut(body: SignOutRequest): Result<MessageResponse>
}
