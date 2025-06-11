package io.chefbook.libs.crypto.encryption

import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import platform.CoreCrypto.CCKeyDerivationPBKDF
import platform.CoreCrypto.kCCPBKDF2
import platform.CoreCrypto.kCCPRFHmacAlgSHA1
import platform.CoreCrypto.kCCSuccess

private const val DerivedKeyLen = AesKeySize / 8

object ССPBKDF2 {

  fun generateSymmetricKey(
    password: String,
    salt: ByteArray,
  ): ByteArray {
    val output = ByteArray(DerivedKeyLen)

    val result = CCKeyDerivationPBKDF(
      algorithm = kCCPBKDF2,
      password = password,
      passwordLen = password.length.convert(),
      salt = salt.asUByteArray().refTo(0),
      saltLen = salt.size.convert(),
      rounds = PBKDF2IterationsCount.convert(),
      prf = kCCPRFHmacAlgSHA1,
      derivedKey = output.asUByteArray().refTo(0),
      derivedKeyLen = DerivedKeyLen.convert(),
    )

    if (result != kCCSuccess) error("Unable to generat PBKDF2 key: $result")

    return output
  }
}
