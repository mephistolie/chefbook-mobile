package io.chefbook.libs.crypto.digest

import io.chefbook.libs.utils.bytes.hexString

val String.sha1String: String get() = sha1.hexString
val String.sha256String: String get() = sha256.hexString

val String.sha1: ByteArray get() = encodeToByteArray().sha1()
val String.sha256: ByteArray get() = encodeToByteArray().sha256()

val ByteArray.sha1: ByteArray get() = sha1()
val ByteArray.sha256: ByteArray get() = sha256()
