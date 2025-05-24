package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth

import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.RefreshTokenRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.exceptions.InvalidRefreshTokenException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

internal class CurrentSessionApiServiceImpl : CurrentSessionApiService {

  override suspend fun refreshSession(
    client: HttpClient,
    body: RefreshTokenRequest,
  ): Result<TokensResponse> = runCatching {
    val result = client.post {
      contentType(ContentType.Application.Json)
      url("$AUTH_ROUTE/refresh")
      setBody(body)
    }

    if (result.status.value in 400..499 && result.status != HttpStatusCode.TooManyRequests) {
      throw InvalidRefreshTokenException
    }

    return@runCatching result.body<TokensResponse>()
  }

  companion object {
    private const val AUTH_ROUTE = "/v1/auth"
  }
}
