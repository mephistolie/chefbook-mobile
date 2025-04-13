package io.chefbook.sdk.network.api.internal.service

import io.chefbook.libs.exceptions.ServerException
import io.chefbook.sdk.network.api.internal.service.dto.responses.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

abstract class RequestHandler {

  protected suspend inline fun <reified T> safeRequest(
    request: () -> HttpResponse,
  ): Result<T> = runCatching {
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
