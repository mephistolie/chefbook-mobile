package io.chefbook.sdk.network.api.internal.service

import io.chefbook.libs.exceptions.ServerException
import io.chefbook.sdk.network.api.internal.service.dto.responses.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

abstract class ChefBookApiService(
  protected val client: HttpClient,
) {

  protected suspend inline fun <reified T> safeGet(requestBuilder: HttpRequestBuilder.() -> Unit): Result<T> =
    safeRequest { client.get(requestBuilder) }

  protected suspend inline fun <reified T> safePost(
    contentType: ContentType = ContentType.Application.Json,
    requestBuilder: HttpRequestBuilder.() -> Unit
  ): Result<T> =
    safeRequest {
      client.post {
        contentType(contentType)
        requestBuilder()
      }
    }

  protected suspend inline fun <reified T> safePut(
    contentType: ContentType = ContentType.Application.Json,
    requestBuilder: HttpRequestBuilder.() -> Unit
  ): Result<T> =
    safeRequest {
      client.put {
        contentType(contentType)
        requestBuilder()
      }
    }

  protected suspend inline fun <reified T> safePatch(
    contentType: ContentType = ContentType.Application.Json,
    requestBuilder: HttpRequestBuilder.() -> Unit
  ): Result<T> =
    safeRequest {
      client.patch {
        contentType(contentType)
        requestBuilder()
      }
    }

  protected suspend inline fun <reified T> safeDelete(requestBuilder: HttpRequestBuilder.() -> Unit): Result<T> =
    safeRequest { client.delete(requestBuilder) }


  protected suspend inline fun <reified T> safeRequest(request: () -> HttpResponse): Result<T> =
    runCatching {
      val response = request()
      return@runCatching if (response.status.isSuccess()) {
        response.body<T>()
      } else {
        val errorBody = response.body<ErrorResponse>()
        throw ServerException(
          code = response.status.value,
          type = errorBody.error,
          message = errorBody.message,
        )
      }
    }
}
