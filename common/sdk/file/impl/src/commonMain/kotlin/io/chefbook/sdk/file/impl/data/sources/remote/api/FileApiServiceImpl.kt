package io.chefbook.sdk.file.impl.data.sources.remote.api

import io.chefbook.libs.exceptions.ServerException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.toByteArray

internal class FileApiServiceImpl(
  private val client: HttpClient,
) : FileApiService {

  override suspend fun getFile(url: String) = runCatching {
    client.get { url(url) }.bodyAsChannel().toByteArray()
  }

  override suspend fun uploadFile(
    url: String,
    body: ByteArray,
    meta: Map<String, String>,
  ): EmptyResult = runCatching {
    val response = client.submitFormWithBinaryData(
      url = url,
      formData = formData {
        meta.forEach { entry -> append(entry.key, entry.value) }
        append("file", body)
      }
    )
    if (response.status.value !in 200..299) {
      throw ServerException(code = (response.status.value), type = ServerException.BIG_FILE)
    }
  }.asEmpty()
}
