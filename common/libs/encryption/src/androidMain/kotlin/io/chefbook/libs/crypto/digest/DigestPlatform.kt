package io.chefbook.libs.crypto.digest

import java.security.MessageDigest

private const val SHA1 = "SHA-1"
private const val SHA256 = "SHA-256"

internal actual fun ByteArray.sha1(): ByteArray = hash(SHA1)
internal actual fun ByteArray.sha256(): ByteArray = hash(SHA256)

private fun ByteArray.hash(algorithm: String): ByteArray =
  MessageDigest
    .getInstance(algorithm)
    .digest(this)
