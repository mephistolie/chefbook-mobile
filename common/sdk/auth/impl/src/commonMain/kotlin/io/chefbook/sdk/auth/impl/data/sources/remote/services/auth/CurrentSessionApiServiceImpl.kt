package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth

import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.RefreshTokenRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto.TokensResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.expetions.InvalidRefreshTokenException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class CurrentSessionApiServiceImpl : CurrentSessionApiService {

  override suspend fun refreshSession(
    client: HttpClient,
    body: RefreshTokenRequest,
  ): Result<TokensResponse> = runCatching {
    withContext(Dispatchers.IO) {
      val result = client.post {
        contentType(ContentType.Application.Json)
        url("$AUTH_ROUTE/refresh")
        setBody(body)
      }

      if (result.status.value in 400..499 && result.status != HttpStatusCode.TooManyRequests) {
        throw InvalidRefreshTokenException
      }

      return@withContext result.body<TokensResponse>()
    }
  }

  companion object {
    private const val AUTH_ROUTE = "/v1/auth"
  }
}
