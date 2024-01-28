package io.chefbook.sdk.file.impl.data.sources.remote

import io.chefbook.sdk.file.impl.data.sources.FileSource
import io.chefbook.sdk.file.impl.data.sources.remote.api.FileApiService

internal class RemoteFileSourceImpl(
  private val api: FileApiService,
) : FileSource {

  override suspend fun getFile(path: String) = api.getFile(url = path)

  override suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String>,
  ) = api.uploadFile(url = path, body = file, meta = meta)
}
