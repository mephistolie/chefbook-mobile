package io.chefbook.libs.utils.hash

import java.security.MessageDigest

actual fun hashString(input: String, algorithm: String): String =
  MessageDigest
    .getInstance(algorithm)
    .digest(input.toByteArray())
    .fold("") { str, it -> str + "%02x".format(it) }
