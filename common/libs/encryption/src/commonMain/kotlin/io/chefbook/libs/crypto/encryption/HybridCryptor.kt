package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.models.CipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey

internal const val AesKeySize = 256
internal const val AesSaltSize = 64

internal const val AesGcmIVLength = 12
internal const val AesGcmTagLength = 16

internal const val PBKDF2IterationsCount = 1_300_000

internal const val RsaKeySize = 4096

expect object HybridCryptor {

  fun generateAsymmetricKey(): AsymmetricKey

  fun generateSymmetricKey(): SymmetricKey

  fun generateSymmetricKey(password: String, salt: ByteArray): SymmetricKey

  fun encryptDataBySymmetricKey(
    data: ByteArray,
    key: SymmetricKey,
    ivSeed: String? = null,
  ): CipherData

  fun decryptDataBySymmetricKey(
    data: CipherData,
    key: SymmetricKey,
  ): ByteArray

  fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray

  fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray

  fun encryptPrivateKeyBySymmetricKey(
    data: AsymmetricPrivateKey,
    key: SymmetricKey,
    ivSeed: String? = null,
  ): CipherData

  fun decryptAsymmetricKeyBySymmetricKey(
    data: CipherData,
    key: SymmetricKey,
  ): AsymmetricKey

  fun encryptSymmetricKeyByPublicKey(data: SymmetricKey, key: AsymmetricPublicKey): ByteArray
  fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: AsymmetricPrivateKey): SymmetricKey
}
