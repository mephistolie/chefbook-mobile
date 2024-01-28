package io.chefbook.libs.encryption

actual object HybridCryptor {

  actual fun generateAsymmetricKey(): AsymmetricKey {
    TODO()
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
