package io.chefbook.libs.encryption

const val AES_SALT_SIZE = 64

expect object HybridCryptor {
  fun generateAsymmetricKey(): AsymmetricKey
  fun generateSymmetricKey(): SymmetricKey
  fun generateSymmetricKey(passphrase: String, salt: ByteArray): SymmetricKey

  fun encryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray
  fun decryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray

  fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray
  fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray

  fun decryptAsymmetricKeyBySymmetricKey(data: ByteArray, key: SymmetricKey): AsymmetricKey

  fun encryptSymmetricKeyByPublicKey(data: SymmetricKey, key: AsymmetricPublicKey): ByteArray
  fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: AsymmetricPrivateKey): SymmetricKey
}
