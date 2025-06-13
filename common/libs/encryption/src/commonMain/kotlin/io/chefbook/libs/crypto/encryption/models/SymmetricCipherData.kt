package io.chefbook.libs.crypto.encryption.models

import io.chefbook.libs.crypto.encryption.AesGcmTagLength

class SymmetricCipherData(
  val ciphertext: ByteArray,
  val tag: ByteArray,
  val iv: ByteArray,
) {

  val combinedData: ByteArray
    get() = ciphertext + tag

  constructor(
    combinedData: ByteArray,
    iv: ByteArray,
  ) : this(
    ciphertext = combinedData.copyOfRange(0, combinedData.size - AesGcmTagLength),
    tag = combinedData.copyOfRange(combinedData.size - AesGcmTagLength, combinedData.size),
    iv = iv,
  )
}
