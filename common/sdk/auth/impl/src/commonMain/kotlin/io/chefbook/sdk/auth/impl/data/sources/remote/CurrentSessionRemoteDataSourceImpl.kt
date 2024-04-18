package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.RefreshTokenRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.toSessionInfo
import io.ktor.client.HttpClient

internal class CurrentSessionRemoteDataSourceImpl(
  private val api: CurrentSessionApiService,
) : CurrentSessionRemoteDataSource {

  override suspend fun refreshSession(client: HttpClient, refreshToken: String) =
    api.refreshSession(client, RefreshTokenRequest(refreshToken))
      .map(TokensResponse::toSessionInfo)
}
