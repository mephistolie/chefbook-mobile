package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.CipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey
import io.chefbook.libs.crypto.encryption.models.createAsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.createAsymmetricPublicKey
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class HybridCryptorTest {

  private val testPlaintext = "Test Тест!#@$%^'*()ぁ \uD83E\uDD17 \uD83D\uDD25"

  private val testSymmetricKey =
    SymmetricKey("SVYDKXGiucwo356fJ5o2GJ/rv24hgfK6tH+nDKORz7Q=".decodeBase64Bytes())
  private val testIvSeed = "test_iv_seed"
  private val testIv = "/NVqFaK4i1feqbrN".decodeBase64Bytes()
  private val testTag = "34Z2ESWBFTcm8RqVfUta/w==".decodeBase64Bytes()
  private val testCiphertext = "8Seh69G0lG34H6T4QQ3rFPj18r3aYtXyROkvEfuDM7wPBhQy".decodeBase64Bytes()

  private val testPBEPassword = "test_password"
  private val testPBESalt = "test_salt".encodeToByteArray()
  private val testPBEKey = "x4UMwwIDk/uV727t/KqorGhhgW8z5+VDfjoK8kqT7cY=".decodeBase64Bytes()

  private val testAsymmetricPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA0o7rab9jgVo3gpyFlQLVudZ/ZS7sydg4nOJyzfRPlGnYdl59ymvTtCdJN2QkC8GjwiuPsXgpgf2sGD5MXt1fpQt7kip9gDTfKXCxcxZd8GQoxOtbqJ++kZdPVAWwDdoe9CQhjJQ95OMIjKcNHRaEyb9ZuEi9a3F1DpFvfYjdO0BxF3xjeW9Os90ianTvfEdtWGBFjV9UDSlwUUI6vvhXfV9xZx0RuTVMyyw765hIPkxqcTo2Z4VCRDZacaxOJw1+37xFp+nzZOQVkv5smHOapL6dtz9DNt9Ys8GBgHUvxYe+hLSwe1agUnH5PeLGDsoPu1XtqNzhy8jbkGpRBI8h1riKrMZtjpgK/keBSoeGYgQlK1ImebbSBMIJFPzu8AnmEwTvGZ/bB6UCUmDC5XExJ3zTaXMW3NNZQEybBNRhiWCJAN3VEy4aRfnLFhlNkWcLGCCY4ZP2l4rBzJ/pc8s2toSmd/vKvGU4TIHtpHgd43pzc3EsFsb3u0yl2uw4iBFeASPPJLdVhYVHeUZNFzd0BkH5+GFSqEgTd510u4pcnymbt2DKYrUi/YwGqtj5Yk/DcdBFNOPlP3tKjVe0tWol4sjYF6HJLgpspeOMv3IOUUC9bT9WC9wcD6SnPKtUgZW7+J9wLgScNS09Qt8d/1j8F42mkerZJhcjhfqPvZSqphcCAwEAAQ==".decodeBase64Bytes()
  private val testAsymmetricPrivateKey = "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".decodeBase64Bytes()

  @Test
  fun symmetricKeyGeneration() {
    HybridCryptor.generateSymmetricKey()
  }

  @Test
  fun passwordSymmetricKeyGeneration() {
    val key = HybridCryptor.generateSymmetricKey(testPBEPassword, testPBESalt)

    print("${testPBEKey.encodeBase64()}\n${key.raw.encodeBase64()}\n")
    assertContentEquals(testPBEKey, key.raw)
  }

  @Test
  fun asymmetricKeyGeneration() {
    HybridCryptor.generateAsymmetricKey()
  }

  @Test
  fun ivGeneration() {
    val encryptedData = HybridCryptor.encryptDataBySymmetricKey(
      data = testPlaintext.encodeToByteArray(),
      key = testSymmetricKey,
      ivSeed = testIvSeed,
    )

    assertContentEquals(testIv, encryptedData.iv)
  }

  @Test
  fun dataDecryptionBySymmetricKey() {
    val cipherData = CipherData(
      ciphertext = testCiphertext,
      iv = testIv,
      tag = testTag,
    )

    val decryptedData = HybridCryptor.decryptDataBySymmetricKey(cipherData, testSymmetricKey)
    val processedStr = decryptedData.decodeToString()

    assertEquals(testPlaintext, processedStr)
  }

  @Test
  fun dataEncryptionDecryptionBySymmetricKey() {
    val key = HybridCryptor.generateSymmetricKey()

    val originStr = testPlaintext

    val encryptedData = HybridCryptor.encryptDataBySymmetricKey(originStr.encodeToByteArray(), key)
    val decryptedData = HybridCryptor.decryptDataBySymmetricKey(encryptedData, key)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }

  @Test
  fun dataEncryptionByPassphraseSymmetricKey() {
    val passphrase = "test key"
    val salt = "salt"

    val originStr = testPlaintext

    val encryptionKey = HybridCryptor.generateSymmetricKey(passphrase, salt.encodeToByteArray())
    val encryptedData =
      HybridCryptor.encryptDataBySymmetricKey(originStr.encodeToByteArray(), encryptionKey)

    val decryptionKey = HybridCryptor.generateSymmetricKey(passphrase, salt.encodeToByteArray())
    val decryptedData = HybridCryptor.decryptDataBySymmetricKey(encryptedData, decryptionKey)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }

  @Test
  fun dataEncryptionByAsymmetricKey() {
    val key = AsymmetricKey(
      public = createAsymmetricPublicKey(testAsymmetricPublicKey),
      private = createAsymmetricPrivateKey(testAsymmetricPrivateKey),
    )

    val originStr = testPlaintext

    val encryptedData =
      HybridCryptor.encryptDataByAsymmetricKey(originStr.encodeToByteArray(), key.public)
    val decryptedData = HybridCryptor.decryptDataByAsymmetricKey(encryptedData, key.private)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }


  @Test
  fun dataEncryptionDecryptionByPredefinedAsymmetricKey() {
    val key = HybridCryptor.generateAsymmetricKey()

    val originStr = testPlaintext

    val encryptedData =
      HybridCryptor.encryptDataByAsymmetricKey(originStr.encodeToByteArray(), key.public)
    val decryptedData = HybridCryptor.decryptDataByAsymmetricKey(encryptedData, key.private)

    val processedStr = decryptedData.decodeToString()

    assertEquals(originStr, processedStr)
  }

  @Test
  fun symmetricKeyEncryptionByAsymmetricKey() {
    val asymmetricKey = HybridCryptor.generateAsymmetricKey()

    val originKey = HybridCryptor.generateSymmetricKey()

    val encryptedData =
      HybridCryptor.encryptSymmetricKeyByPublicKey(originKey, asymmetricKey.public)
    val processedKey =
      HybridCryptor.decryptSymmetricKeyByPrivateKey(encryptedData, asymmetricKey.private)

    assertContentEquals(originKey.raw, processedKey.raw)
  }

  @Test
  fun asymmetricKeyEncryptionBySymmetricKey() {
    val symmetricKey = HybridCryptor.generateSymmetricKey()

    val originKey = HybridCryptor.generateAsymmetricKey()

    val encryptedData =
      HybridCryptor.encryptPrivateKeyBySymmetricKey(originKey.private, symmetricKey)
    val processedKey = HybridCryptor.decryptAsymmetricKeyBySymmetricKey(encryptedData, symmetricKey)

    assertContentEquals(originKey.public.raw, processedKey.public.raw)
    assertContentEquals(originKey.private.raw, processedKey.private.raw)
  }
}
