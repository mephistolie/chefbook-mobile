package io.chefbook.sdk.file.api.internal.io

import okio.FileSystem
import okio.Path

interface IOProvider {

  val fileSystem: FileSystem

  val filesDir: Path

  val cacheDir: Path
}