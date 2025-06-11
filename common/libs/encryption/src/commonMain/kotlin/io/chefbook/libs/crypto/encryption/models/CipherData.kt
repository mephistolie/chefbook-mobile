package io.chefbook.libs.crypto.encryption.models

class CipherData(
  val ciphertext: ByteArray,
  val iv: ByteArray,
  val tag: ByteArray,
)