package io.chefbook.libs.utils.interop

import kotlinx.cinterop.memScoped
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

val ByteArray.nSData: NSData
  get() = memScoped {
    NSData.create(
      bytes = allocArrayOf(this@nSData),
      length = this@nSData.size.toULong(),
    )
  }

val NSData.byteArray: ByteArray
  get() = ByteArray(this@byteArray.length.toInt()).apply {
    usePinned {
      memcpy(it.addressOf(0), this@byteArray.bytes, this@byteArray.length)
    }
  }
