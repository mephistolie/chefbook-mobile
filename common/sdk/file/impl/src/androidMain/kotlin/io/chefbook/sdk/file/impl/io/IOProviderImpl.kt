package io.chefbook.sdk.file.impl.io

import android.content.Context
import io.chefbook.sdk.file.api.internal.io.IOProvider
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath

class IOProviderImpl(
  context: Context,
) : IOProvider {

  override val fileSystem: FileSystem
    get() = FileSystem.Companion.SYSTEM

  override val filesDir: Path = context.filesDir.toOkioPath()

  override val cacheDir: Path = context.cacheDir.toOkioPath()
}