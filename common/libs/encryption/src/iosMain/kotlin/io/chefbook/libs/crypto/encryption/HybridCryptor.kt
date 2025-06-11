package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.utils.interop.byteArray
import io.chefbook.libs.utils.interop.nSData
import io.chefbook.libs.crypto.digest.sha256
import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.models.CipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey
import io.chefbook.libs.crypto.random.CCRandom
import io.chefbook.libs.utils.interop.swiftTry
import platform.Foundation.NSData

actual object HybridCryptor {

  private val randomIV: ByteArray
    get() = CCRandom.nextBytes(AesGcmIVLength)

  actual fun generateAsymmetricKey(): AsymmetricKey {
    val (publicKey, privateKey) = ССRSA.generateKey()
    return AsymmetricKey(
      public = AsymmetricPublicKey.pkcs1(publicKey.byteArray),
      private = AsymmetricPrivateKey.pkcs1(privateKey.byteArray),
    )
  }

  actual fun generateSymmetricKey(): SymmetricKey =
    SymmetricKey(CryptoKitAes.generateKey().byteArray)

  actual fun generateSymmetricKey(
    password: String,
    salt: ByteArray,
  ): SymmetricKey {
    return SymmetricKey(
      raw = ССPBKDF2.generateSymmetricKey(
        password = password,
        salt = salt,
      )
    )
  }

  actual fun encryptDataBySymmetricKey(
    data: ByteArray,
    key: SymmetricKey,
    ivSeed: String?,
  ): CipherData {
    val iv = ivSeed?.sha256?.copyOfRange(0, AesGcmIVLength) ?: randomIV

    val result = swiftTry { e ->
      CryptoKitAes.encryptGCMWithKey(
        plaintext = data.nSData,
        key = key.raw.nSData,
        iv = iv.nSData,
        error = e,
      )
    }

    val ciphertext = (result[0] as NSData).byteArray
    val tag = (result[1] as NSData).byteArray

    return CipherData(
      ciphertext = ciphertext,
      iv = iv,
      tag = tag,
    )
  }

  actual fun decryptDataBySymmetricKey(
    data: CipherData,
    key: SymmetricKey,
  ): ByteArray =
    swiftTry { e ->
      CryptoKitAes.decryptGCMWithKey(
        ciphertext = data.ciphertext.nSData,
        key = key.raw.nSData,
        iv = data.iv.nSData,
        tag = data.tag.nSData,
        error = e,
      )
    }.byteArray

  actual fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray {
    return ССRSA.encryptWithPublicKey(
      publicKey = key.pkcs1.nSData,
      plaintext = data.nSData,
    ).byteArray
  }

  actual fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    return ССRSA.decryptWithPrivateKey(
      privateKey = key.pkcs1.nSData,
      ciphertext = data.nSData,
    ).byteArray
  }

  actual fun encryptPrivateKeyBySymmetricKey(
    data: AsymmetricPrivateKey,
    key: SymmetricKey,
    ivSeed: String?,
  ): CipherData = encryptDataBySymmetricKey(data.raw, key, ivSeed)

  actual fun decryptAsymmetricKeyBySymmetricKey(
    data: CipherData,
    key: SymmetricKey,
  ): AsymmetricKey {
    val privateKey = AsymmetricPrivateKey.pkcs8(decryptDataBySymmetricKey(data, key))
    val publicKey = AsymmetricPublicKey.pkcs1(ССRSA.getPublicKey(privateKey.pkcs1.nSData).byteArray)
    return AsymmetricKey(
      public = publicKey,
      private = privateKey,
    )
  }

  actual fun encryptSymmetricKeyByPublicKey(
    data: SymmetricKey,
    key: AsymmetricPublicKey
  ): ByteArray = encryptDataByAsymmetricKey(data.raw, key)

  actual fun decryptSymmetricKeyByPrivateKey(
    data: ByteArray,
    key: AsymmetricPrivateKey
  ): SymmetricKey = SymmetricKey(decryptDataByAsymmetricKey(data, key))
}
