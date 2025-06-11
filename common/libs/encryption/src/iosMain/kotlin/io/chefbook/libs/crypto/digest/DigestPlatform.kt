package io.chefbook.libs.crypto.digest

import org.kotlincrypto.hash.sha1.SHA1
import org.kotlincrypto.hash.sha2.SHA256

internal actual fun ByteArray.sha1(): ByteArray = SHA1().digest(this)

internal actual fun ByteArray.sha256(): ByteArray = SHA256().digest(this)
