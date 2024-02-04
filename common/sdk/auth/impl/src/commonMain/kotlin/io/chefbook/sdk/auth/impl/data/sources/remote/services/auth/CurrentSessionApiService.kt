package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth

import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.RefreshTokenRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.ktor.client.HttpClient

internal interface CurrentSessionApiService {

  suspend fun refreshSession(
    client: HttpClient,
    body: RefreshTokenRequest
  ): Result<TokensResponse>
}
