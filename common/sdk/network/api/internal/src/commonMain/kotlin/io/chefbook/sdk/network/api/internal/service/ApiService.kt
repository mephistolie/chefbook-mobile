package io.chefbook.sdk.network.api.internal.service

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.contentType

abstract class ApiService(
  protected val client: HttpClient,
) : RequestHandler() {

  protected suspend inline fun <reified T> safeGet(
    url: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
  ): Result<T> =
    safeRequest { client.get(url, requestBuilder) }

  protected suspend inline fun <reified T> safePost(
    url: String,
    contentType: ContentType = ContentType.Application.Json,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
  ): Result<T> =
    safeRequest {
      client.post(url) {
        contentType(contentType)
        requestBuilder()
      }
    }

  protected suspend inline fun <reified T> safePut(
    url: String,
    contentType: ContentType = ContentType.Application.Json,
    requestBuilder: HttpRequestBuilder.() -> Unit
  ): Result<T> =
    safeRequest {
      client.put(url) {
        contentType(contentType)
        requestBuilder()
      }
    }

  protected suspend inline fun <reified T> safePatch(
    url: String,
    contentType: ContentType = ContentType.Application.Json,
    requestBuilder: HttpRequestBuilder.() -> Unit
  ): Result<T> =
    safeRequest {
      client.patch(url) {
        contentType(contentType)
        requestBuilder()
      }
    }

  protected suspend inline fun <reified T> safeDelete(
    url: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {},
  ): Result<T> =
    safeRequest { client.delete(url, requestBuilder) }
}
