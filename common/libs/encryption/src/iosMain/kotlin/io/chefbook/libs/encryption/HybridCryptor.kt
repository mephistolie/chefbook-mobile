package io.chefbook.libs.encryption

import cocoapods.Tink.TINKAeadKeyTemplate
import cocoapods.Tink.TINKAes128Eax
import cocoapods.Tink.TINKAes128Gcm
import cocoapods.Tink.TINKEciesP256HkdfHmacSha256Aes128CtrHmacSha256
import cocoapods.Tink.TINKEciesP256HkdfHmacSha256Aes128Gcm
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual object HybridCryptor {

  actual fun generateAsymmetricKey(): AsymmetricKey {
    TINKEciesP256HkdfHmacSha256Aes128Gcm
    TINKAeadKeyTemplate().initWithKeyTemplate(TINKEciesP256HkdfHmacSha256Aes128Gcm)?.
  }

  actual fun generateSymmetricKey(): SymmetricKey {
    TODO()
  }

  actual fun generateSymmetricKey(passphrase: String, salt: ByteArray): SymmetricKey {
    TODO()
  }

  actual fun encryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray {
    TODO()
  }

  actual fun decryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray {
    TODO()
  }

  actual fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray {
    TODO()
  }

  actual fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    TODO()
  }

  actual fun decryptAsymmetricKeyBySymmetricKey(data: ByteArray, key: SymmetricKey): AsymmetricKey {
    TODO()
  }

  actual fun encryptSymmetricKeyByPublicKey(data: SymmetricKey, key: AsymmetricPublicKey): ByteArray {
    TODO()
  }

  actual fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: AsymmetricPrivateKey): SymmetricKey {
    TODO()
  }
}
