package io.chefbook.sdk.file.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult

internal interface FileSource {

  suspend fun getFile(path: String): Result<ByteArray>

  suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String> = emptyMap(),
  ): EmptyResult
}
