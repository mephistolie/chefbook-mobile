package io.chefbook.sdk.file.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.file.impl.data.sources.FileSource

internal interface LocalFileSource : FileSource {

  suspend fun deleteFile(path: String): EmptyResult

  suspend fun deleteCachedFile(relativePath: String): EmptyResult
}
