package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.utils.interop.byteArray
import io.chefbook.libs.utils.interop.nSData
import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey
import io.chefbook.libs.crypto.random.CCRandom
import io.chefbook.libs.utils.interop.swiftTry
import platform.Foundation.NSData

actual object HybridCryptor {

  actual fun generateAsymmetricKey(): AsymmetricKey {
    val (publicKey, privateKey) = ССRSA.generateKey()
    return AsymmetricKey(
      public = AsymmetricPublicKey.pkcs1(publicKey.byteArray),
      private = AsymmetricPrivateKey.pkcs1(privateKey.byteArray),
    )
  }

  actual fun generatePasswordSymmetricKey(): SymmetricKey =
    SymmetricKey(CryptoKitAes.generateKey().byteArray)

  actual fun generateRandomIV(): ByteArray = CCRandom.nextBytes(AesGcmIVLength)

  actual fun generateSalt(): ByteArray = CCRandom.nextBytes(AesSaltSize)

  actual fun generatePasswordSymmetricKey(
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

  actual fun encryptBySymmetricKey(
    plaintext: ByteArray,
    key: SymmetricKey,
    iv: ByteArray,
  ): SymmetricCipherData {
    val result = swiftTry { e ->
      CryptoKitAes.encryptGCMWithKey(
        plaintext = plaintext.nSData,
        key = key.raw.nSData,
        iv = iv.nSData,
        error = e,
      )
    }

    val ciphertext = (result[0] as NSData).byteArray
    val tag = (result[1] as NSData).byteArray

    return SymmetricCipherData(
      ciphertext = ciphertext,
      iv = iv,
      tag = tag,
    )
  }

  actual fun decryptBySymmetricKey(
    cipherData: SymmetricCipherData,
    key: SymmetricKey,
  ): ByteArray =
    swiftTry { e ->
      CryptoKitAes.decryptGCMWithKey(
        ciphertext = cipherData.ciphertext.nSData,
        key = key.raw.nSData,
        iv = cipherData.iv.nSData,
        tag = cipherData.tag.nSData,
        error = e,
      )
    }.byteArray

  actual fun encryptByAsymmetricKey(plaintext: ByteArray, key: AsymmetricPublicKey): ByteArray {
    return ССRSA.encryptWithPublicKey(
      publicKey = key.pkcs1.nSData,
      plaintext = plaintext.nSData,
    ).byteArray
  }

  actual fun decryptByAsymmetricKey(ciphertext: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    return ССRSA.decryptWithPrivateKey(
      privateKey = key.pkcs1.nSData,
      ciphertext = ciphertext.nSData,
    ).byteArray
  }

  actual fun decryptAsymmetricKeyBySymmetricKey(
    cipherData: SymmetricCipherData,
    key: SymmetricKey,
  ): AsymmetricKey {
    val privateKey = AsymmetricPrivateKey.pkcs8(decryptBySymmetricKey(cipherData, key))
    val publicKey = AsymmetricPublicKey.pkcs1(ССRSA.getPublicKey(privateKey.pkcs1.nSData).byteArray)
    return AsymmetricKey(
      public = publicKey,
      private = privateKey,
    )
  }
}
