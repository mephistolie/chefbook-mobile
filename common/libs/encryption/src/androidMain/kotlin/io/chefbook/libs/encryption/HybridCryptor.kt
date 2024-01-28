package io.chefbook.libs.encryption

import org.spongycastle.asn1.DERNull
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.spongycastle.asn1.x509.AlgorithmIdentifier
import org.spongycastle.jcajce.spec.PBKDF2KeySpec
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec

actual object HybridCryptor {

  private val IV = "chefbookchefbook".toByteArray()
  private const val TAG_LENGTH = 16

  private const val AES = "AES"
  private const val AES_KEY_LENGTH = 256
  internal const val AES_GCM = "AES/GCM/NoPadding"
  private const val AES_SIZE = 256
  private const val AES_ITERATIONS_COUNT = 10000

  private const val RSA = "RSA"
  private const val RSA_LENGTH = 4096

  private val aesGenerator = KeyGenerator.getInstance(AES)
  private val aesCipher = Cipher.getInstance(AES_GCM)
  private val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
  private val pfr =
    AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA256, DERNull.INSTANCE);
  private val spec = GCMParameterSpec(TAG_LENGTH * 8, IV)

  private val rsaGenerator = KeyPairGenerator.getInstance(RSA)
  internal val rsaFactory = KeyFactory.getInstance(RSA)
  private val rsaCipher = Cipher.getInstance(RSA)
  
  actual fun generateAsymmetricKey(): AsymmetricKey {
    rsaGenerator.initialize(RSA_LENGTH)
    return rsaGenerator.generateKeyPair().asAsymmetricKey()
  }

  actual fun generateSymmetricKey(): SymmetricKey {
    aesGenerator.init(AES_SIZE)
    return aesGenerator.generateKey().asSymmetricKey()
  }

  actual fun generateSymmetricKey(passphrase: String, salt: ByteArray): SymmetricKey {
    val spec =
      PBKDF2KeySpec(passphrase.toCharArray(), salt, AES_ITERATIONS_COUNT, AES_KEY_LENGTH, pfr)
    return keyFactory.generateSecret(spec).asSymmetricKey()
  }

  actual fun encryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray {
    aesCipher.init(Cipher.ENCRYPT_MODE, key.asSecretKey(), spec)
    return aesCipher.doFinal(data)
  }

  actual fun decryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray {
    aesCipher.init(Cipher.DECRYPT_MODE, key.asSecretKey(), spec)
    return aesCipher.doFinal(data)
  }

  actual fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray {
    rsaCipher.init(Cipher.ENCRYPT_MODE, key.asPublicKey())
    return rsaCipher.doFinal(data)
  }

  actual fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    rsaCipher.init(Cipher.DECRYPT_MODE, key.asPrivateKey())
    return rsaCipher.doFinal(data)
  }

  actual fun decryptAsymmetricKeyBySymmetricKey(data: ByteArray, key: SymmetricKey): AsymmetricKey {
    aesCipher.init(Cipher.DECRYPT_MODE, key.asSecretKey(), spec)
    val privateKeyBytes = aesCipher.doFinal(data)
    val privateKey =
      rsaFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateCrtKey
    val publicKey =
      rsaFactory.generatePublic(RSAPublicKeySpec(privateKey.modulus, privateKey.publicExponent))
    return KeyPair(publicKey, privateKey).asAsymmetricKey()
  }

  actual fun encryptSymmetricKeyByPublicKey(data: SymmetricKey, key: AsymmetricPublicKey): ByteArray {
    return encryptDataByAsymmetricKey(data.raw, key)
  }

  actual fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: AsymmetricPrivateKey): SymmetricKey {
    return SymmetricKey(decryptDataByAsymmetricKey(data, key))
  }
}
