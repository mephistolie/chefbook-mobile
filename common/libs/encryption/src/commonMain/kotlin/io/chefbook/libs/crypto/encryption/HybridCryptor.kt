package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.crypto.digest.sha256
import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey

internal const val AesKeySize = 256
internal const val AesSaltSize = 64

internal const val AesGcmIVLength = 12
internal const val AesGcmTagLength = 16

internal const val PBKDF2IterationsCount = 1_300_000
internal const val PBKDF2SaltSize = 16

internal const val RsaKeySize = 4096

expect object HybridCryptor {

  fun generateAsymmetricKey(): AsymmetricKey

  fun generatePasswordSymmetricKey(): SymmetricKey

  fun generatePasswordSymmetricKey(
    password: String,
    salt: ByteArray,
  ): SymmetricKey

  fun generateRandomIV(): ByteArray

  fun generateSalt(): ByteArray

  fun encryptBySymmetricKey(
    plaintext: ByteArray,
    key: SymmetricKey,
    iv: ByteArray,
  ): SymmetricCipherData

  fun decryptBySymmetricKey(
    cipherData: SymmetricCipherData,
    key: SymmetricKey,
  ): ByteArray

  fun encryptByAsymmetricKey(plaintext: ByteArray, key: AsymmetricPublicKey): ByteArray

  fun decryptByAsymmetricKey(ciphertext: ByteArray, key: AsymmetricPrivateKey): ByteArray

  fun decryptAsymmetricKeyBySymmetricKey(
    cipherData: SymmetricCipherData,
    key: SymmetricKey,
  ): AsymmetricKey
}

fun HybridCryptor.encryptPrivateKeyBySymmetricKey(
  privateKey: AsymmetricPrivateKey,
  symmetricKey: SymmetricKey,
  iv: ByteArray,
): SymmetricCipherData = encryptBySymmetricKey(
  plaintext = privateKey.raw,
  key = symmetricKey,
  iv = iv,
)

fun HybridCryptor.encryptSymmetricKeyByPublicKey(
  symmetricKey: SymmetricKey,
  publicKey: AsymmetricPublicKey,
): ByteArray = encryptByAsymmetricKey(
  plaintext = symmetricKey.raw,
  key = publicKey,
)

fun HybridCryptor.decryptSymmetricKeyByPrivateKey(
  ciphertext: ByteArray,
  key: AsymmetricPrivateKey,
): SymmetricKey = SymmetricKey(
  raw = decryptByAsymmetricKey(
    ciphertext = ciphertext,
    key = key,
  ),
)

fun HybridCryptor.generateIV(seed: String): ByteArray =
  seed.sha256.copyOfRange(0, AesGcmIVLength)
