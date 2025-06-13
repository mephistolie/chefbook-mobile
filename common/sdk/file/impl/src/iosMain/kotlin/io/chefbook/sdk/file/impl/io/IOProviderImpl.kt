package io.chefbook.sdk.file.impl.io

import io.chefbook.sdk.file.api.internal.io.IOProvider
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
class IOProviderImpl : IOProvider {

  override val fileSystem: FileSystem
    get() = FileSystem.Companion.SYSTEM

  override val filesDir: Path
    get() = requireNotNull(
      NSFileManager.Companion.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
      )?.path
    ).toPath()

  override val cacheDir: Path
    get() = requireNotNull(
      NSFileManager.Companion.defaultManager.URLForDirectory(
        directory = NSCachesDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
      )?.path
    ).toPath()
}