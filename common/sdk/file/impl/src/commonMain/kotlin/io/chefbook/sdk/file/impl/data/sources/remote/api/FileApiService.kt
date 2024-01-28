package io.chefbook.sdk.file.impl.data.sources.remote.api

import io.chefbook.libs.utils.result.EmptyResult

internal interface FileApiService {

  suspend fun getFile(url: String): Result<ByteArray>

  suspend fun uploadFile(
    url: String,
    body: ByteArray,
    meta: Map<String, String>,
  ): EmptyResult
}
