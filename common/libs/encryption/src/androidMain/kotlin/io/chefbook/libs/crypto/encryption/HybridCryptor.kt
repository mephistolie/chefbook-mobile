package io.chefbook.libs.crypto.encryption

import io.chefbook.libs.crypto.encryption.models.AsymmetricKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPrivateKey
import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
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

  actual fun generatePasswordSymmetricKey(): SymmetricKey {
    aesGenerator.init(AesKeySize)
    return aesGenerator.generateKey().asSymmetricKey()
  }

  actual fun generateRandomIV(): ByteArray = SecureRandom().generateSeed(AesGcmIVLength)

  actual fun generateSalt(): ByteArray = SecureRandom().generateSeed(AesSaltSize)

  actual fun generatePasswordSymmetricKey(
    password: String,
    salt: ByteArray,
  ): SymmetricKey {
    val spec = PBEKeySpec(password.toCharArray(), salt, PBKDF2IterationsCount, AesKeySize)
    return keyFactory.generateSecret(spec).asSymmetricKey()
  }

  actual fun encryptBySymmetricKey(
    plaintext: ByteArray,
    key: SymmetricKey,
    iv: ByteArray,
  ): SymmetricCipherData {
    val spec = GCMParameterSpec(AesGcmTagLengthBits, iv)

    aesCipher.init(Cipher.ENCRYPT_MODE, key.asSecretKey(), spec)
    val output = aesCipher.doFinal(plaintext)

    val ciphertext = output.copyOfRange(0, output.size - AesGcmTagLength)
    val tag = output.copyOfRange(output.size - AesGcmTagLength, output.size)

    return SymmetricCipherData(
      ciphertext = ciphertext,
      iv = iv,
      tag = tag,
    )
  }

  actual fun decryptBySymmetricKey(
    cipherData: SymmetricCipherData,
    key: SymmetricKey,
  ): ByteArray {
    val spec = GCMParameterSpec(AesGcmTagLengthBits, cipherData.iv)

    aesCipher.init(Cipher.DECRYPT_MODE, key.asSecretKey(), spec)

    return aesCipher.doFinal(cipherData.ciphertext + cipherData.tag)
  }

  actual fun encryptByAsymmetricKey(plaintext: ByteArray, key: AsymmetricPublicKey): ByteArray {
    rsaCipher.init(Cipher.ENCRYPT_MODE, key.asPublicKey())
    return rsaCipher.doFinal(plaintext)
  }

  actual fun decryptByAsymmetricKey(ciphertext: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    rsaCipher.init(Cipher.DECRYPT_MODE, key.asPrivateKey())
    return rsaCipher.doFinal(ciphertext)
  }

  actual fun decryptAsymmetricKeyBySymmetricKey(
    cipherData: SymmetricCipherData,
    key: SymmetricKey
  ): AsymmetricKey {
    val privateKeyBytes = decryptBySymmetricKey(cipherData, key)
    val privateKey =
      rsaFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateCrtKey
    val publicKey =
      rsaFactory.generatePublic(RSAPublicKeySpec(privateKey.modulus, privateKey.publicExponent))
    return KeyPair(publicKey, privateKey).asAsymmetricKey()
  }
}
