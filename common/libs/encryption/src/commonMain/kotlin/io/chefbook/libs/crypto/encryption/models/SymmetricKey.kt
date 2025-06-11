package io.chefbook.libs.crypto.encryption.models

import kotlin.jvm.JvmInline

@JvmInline
value class SymmetricKey(
  val raw: ByteArray
)
