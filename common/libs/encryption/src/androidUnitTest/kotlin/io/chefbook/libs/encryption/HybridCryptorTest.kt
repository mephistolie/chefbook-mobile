package io.chefbook.libs.encryption

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.hybrid.HybridConfig
import junit.framework.TestCase.assertEquals
import org.junit.Test

class HybridCryptorTest {

  @Test
  fun symmetricKeyGeneration() {
    AeadConfig.register()

    HybridCryptor.generateSymmetricKey()
  }

  @Test
  fun passphraseSymmetricKeyGeneration() {
    AeadConfig.register()

    val passphrase = "test"

    HybridCryptor.generateSymmetricKey(passphrase, ByteArray(0))
  }

  @Test
  fun asymmetricKeyGeneration() {
    HybridConfig.register()

    HybridCryptor.generateAsymmetricKey()
  }

  @Test
  fun dataEncryptionBySymmetricKey() {
    AeadConfig.register()

    val key = HybridCryptor.generateSymmetricKey()

    val originStr = "test"

    val encryptedData = HybridCryptor.encryptDataBySymmetricKey(originStr.encodeToByteArray(), key)
    val decryptedData = HybridCryptor.decryptDataBySymmetricKey(encryptedData, key)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }

  @Test
  fun dataEncryptionByPassphraseSymmetricKey() {
    AeadConfig.register()
    HybridConfig.register()

    val passphare = "test_key"
    val salt = "salt"

    val originStr = "test"

    val encryptionKey = HybridCryptor.generateSymmetricKey(passphare, salt.encodeToByteArray())
    val encryptedData = HybridCryptor.encryptDataBySymmetricKey(originStr.encodeToByteArray(), encryptionKey)

    val decryptionKey = HybridCryptor.generateSymmetricKey(passphare, salt.encodeToByteArray())
    val decryptedData = HybridCryptor.decryptDataBySymmetricKey(encryptedData, decryptionKey)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }

  @Test
  fun dataEncryptionByAsymmetricKey() {
    HybridConfig.register()

    val key = HybridCryptor.generateAsymmetricKey()

    val originStr = "test"

    val encryptedData =
      HybridCryptor.encryptDataByAsymmetricKey(originStr.encodeToByteArray(), key.public)
    val decryptedData = HybridCryptor.decryptDataByAsymmetricKey(encryptedData, key.private)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }

  @Test
  fun symmetricKeyEncryptionByAsymmetricKey() {
    AeadConfig.register()
    HybridConfig.register()

    val asymmetricKey = HybridCryptor.generateAsymmetricKey()

    val originKey = HybridCryptor.generateSymmetricKey()

    val encryptedData =
      HybridCryptor.encryptSymmetricKeyByPublicKey(originKey, asymmetricKey.public)
    val processedKey =
      HybridCryptor.decryptSymmetricKeyByPrivateKey(encryptedData, asymmetricKey.private)

    assertEquals(
      originKey.asSecretKey().keysetInfo.primaryKeyId,
      processedKey.asSecretKey().keysetInfo.primaryKeyId,
    )
  }

  @Test
  fun asymmetricKeyEncryptionBySymmetricKey() {
    AeadConfig.register()
    HybridConfig.register()

    val symmetricKey = HybridCryptor.generateSymmetricKey()

    val originKey = HybridCryptor.generateAsymmetricKey()

    val encryptedData =
      HybridCryptor.encryptPrivateKeyBySymmetricKey(originKey.private, symmetricKey)
    val processedKey = HybridCryptor.decryptAsymmetricKeyBySymmetricKey(encryptedData, symmetricKey)

    assertEquals(
      originKey.private.asPrivateKey().keysetInfo.primaryKeyId,
      processedKey.private.asPrivateKey().keysetInfo.primaryKeyId,
    )
  }
}