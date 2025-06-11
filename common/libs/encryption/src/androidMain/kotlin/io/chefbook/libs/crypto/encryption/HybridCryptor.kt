package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.crypto.digest.sha256
import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.models.CipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey
import io.chefbook.libs.crypto.encryption.models.asAsymmetricKey
import io.chefbook.libs.crypto.encryption.models.asPrivateKey
import io.chefbook.libs.crypto.encryption.models.asPublicKey
import io.chefbook.libs.crypto.encryption.models.asSecretKey
import io.chefbook.libs.crypto.encryption.models.asSymmetricKey
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec

actual object HybridCryptor {

  private val randomIV: ByteArray
    get() = SecureRandom().generateSeed(AesGcmIVLength)

  private val aesGenerator = KeyGenerator.getInstance(Aes)
  private val aesCipher = Cipher.getInstance(AesGcm)

  private val rsaGenerator = KeyPairGenerator.getInstance(Rsa)
  internal val rsaFactory = KeyFactory.getInstance(Rsa)
  private val rsaCipher = Cipher.getInstance(RsaEcbOaep)

  private val keyFactory = SecretKeyFactory.getInstance(PBKDF2WithHmacSHA1)

  actual fun generateAsymmetricKey(): AsymmetricKey {
    rsaGenerator.initialize(RsaKeySize)
    return rsaGenerator.generateKeyPair().asAsymmetricKey()
  }

  actual fun generateSymmetricKey(): SymmetricKey {
    aesGenerator.init(AesKeySize)
    return aesGenerator.generateKey().asSymmetricKey()
  }

  actual fun generateSymmetricKey(password: String, salt: ByteArray): SymmetricKey {
    val spec = PBEKeySpec(password.toCharArray(), salt, PBKDF2IterationsCount, AesKeySize)
    return keyFactory.generateSecret(spec).asSymmetricKey()
  }

  actual fun encryptDataBySymmetricKey(
    data: ByteArray,
    key: SymmetricKey,
    ivSeed: String?,
  ): CipherData {
    val iv = ivSeed?.sha256?.copyOfRange(0, AesGcmIVLength) ?: randomIV
    val spec = GCMParameterSpec(AesGcmTagLengthBits, iv)

    aesCipher.init(Cipher.ENCRYPT_MODE, key.asSecretKey(), spec)
    val output = aesCipher.doFinal(data)

    val ciphertext = output.copyOfRange(0, output.size - AesGcmTagLength)
    val tag = output.copyOfRange(output.size - AesGcmTagLength, output.size)

    return CipherData(
      ciphertext = ciphertext,
      iv = iv,
      tag = tag,
    )
  }

  actual fun decryptDataBySymmetricKey(
    data: CipherData,
    key: SymmetricKey,
  ): ByteArray {
    val spec = GCMParameterSpec(AesGcmTagLengthBits, data.iv)

    aesCipher.init(Cipher.DECRYPT_MODE, key.asSecretKey(), spec)

    return aesCipher.doFinal(data.ciphertext + data.tag)
  }

  actual fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray {
    rsaCipher.init(Cipher.ENCRYPT_MODE, key.asPublicKey())
    return rsaCipher.doFinal(data)
  }

  actual fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    rsaCipher.init(Cipher.DECRYPT_MODE, key.asPrivateKey())
    return rsaCipher.doFinal(data)
  }

  actual fun encryptPrivateKeyBySymmetricKey(
    data: AsymmetricPrivateKey,
    key: SymmetricKey,
    ivSeed: String?,
  ): CipherData = encryptDataBySymmetricKey(data.raw, key, ivSeed)

  actual fun decryptAsymmetricKeyBySymmetricKey(
    data: CipherData,
    key: SymmetricKey
  ): AsymmetricKey {
    val privateKeyBytes = decryptDataBySymmetricKey(data, key)
    val privateKey =
      rsaFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateCrtKey
    val publicKey =
      rsaFactory.generatePublic(RSAPublicKeySpec(privateKey.modulus, privateKey.publicExponent))
    return KeyPair(publicKey, privateKey).asAsymmetricKey()
  }

  actual fun encryptSymmetricKeyByPublicKey(
    data: SymmetricKey,
    key: AsymmetricPublicKey
  ): ByteArray {
    return encryptDataByAsymmetricKey(data.raw, key)
  }

  actual fun decryptSymmetricKeyByPrivateKey(
    data: ByteArray,
    key: AsymmetricPrivateKey
  ): SymmetricKey {
    return SymmetricKey(decryptDataByAsymmetricKey(data, key))
  }
}
