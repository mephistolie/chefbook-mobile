package io.chefbook.libs.crypto.random

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCRandomGenerateBytes
import platform.CoreCrypto.kCCSuccess

object CCRandom {

  fun nextBytes(size: Int): ByteArray {
    val array = ByteArray(size)

    val result = array.usePinned { pinned ->
      CCRandomGenerateBytes(pinned.addressOf(0), size.convert())
    }

    if (result != kCCSuccess) error("Generate bytes failed: $result")

    return array
  }
}