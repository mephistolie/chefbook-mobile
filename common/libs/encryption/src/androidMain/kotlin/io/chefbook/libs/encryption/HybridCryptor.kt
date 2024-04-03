package io.chefbook.libs.encryption

import com.google.crypto.tink.Aead
import com.google.crypto.tink.HybridDecrypt
import com.google.crypto.tink.HybridEncrypt
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.google.crypto.tink.aead.AesGcmKey
import com.google.crypto.tink.aead.AesGcmParameters
import com.google.crypto.tink.aead.PredefinedAeadParameters.AES256_GCM
import com.google.crypto.tink.hybrid.PredefinedHybridParameters.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM
import com.google.crypto.tink.util.SecretBytes
import java.security.MessageDigest

actual object HybridCryptor {

  private val EMPTY_ASSOCIATED_DATA = ByteArray(0)

  private val passphraseSymmetricKeyParams = AesGcmParameters.builder()
      .setKeySizeBytes(32)
      .setIvSizeBytes(12)
      .setTagSizeBytes(16)
      .build()
  actual fun generateAsymmetricKey(): AsymmetricKey {
    val keyset = KeysetHandle.generateNew(ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM)
    return keyset.asAsymmetricKey()
  }

  actual fun generateSymmetricKey(): SymmetricKey {
    val keyset = KeysetHandle.generateNew(AES256_GCM)
    return keyset.asSymmetricKey()
  }

  actual fun generateSymmetricKey(passphrase: String, salt: ByteArray): SymmetricKey {
    val md = MessageDigest.getInstance("SHA-256")
    val digest: ByteArray = md.digest(passphrase.toByteArray() + salt)

    val key = AesGcmKey.builder()
      .setKeyBytes(SecretBytes.copyFrom(digest, InsecureSecretKeyAccess.get()))
      .setParameters(passphraseSymmetricKeyParams)
      .build()

    val entry = KeysetHandle.importKey(key)
      .withRandomId()
      .makePrimary()

    return KeysetHandle.newBuilder()
      .addEntry(entry)
      .build()
      .asSymmetricKey()
  }

  actual fun encryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray {
    val keyset = key.asSecretKey()
    val aead = keyset.getPrimitive(Aead::class.java)
    return aead.encrypt(data, EMPTY_ASSOCIATED_DATA)
  }

  actual fun decryptDataBySymmetricKey(data: ByteArray, key: SymmetricKey): ByteArray {
    val keyset = key.asSecretKey()
    val aead = keyset.getPrimitive(Aead::class.java)
    return aead.decrypt(data, EMPTY_ASSOCIATED_DATA)
  }

  actual fun encryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPublicKey): ByteArray {
    val keyset = key.asPublicKey()
    val hybridEncrypt = keyset.getPrimitive(HybridEncrypt::class.java)
    return hybridEncrypt.encrypt(data, EMPTY_ASSOCIATED_DATA)
  }

  actual fun decryptDataByAsymmetricKey(data: ByteArray, key: AsymmetricPrivateKey): ByteArray {
    val keyset = key.asPrivateKey()
    val hybridDecrypt = keyset.getPrimitive(HybridDecrypt::class.java)
    return hybridDecrypt.decrypt(data, EMPTY_ASSOCIATED_DATA)
  }

  actual fun encryptPrivateKeyBySymmetricKey(data: AsymmetricPrivateKey, key: SymmetricKey): ByteArray {
    val symmetricKeyset = key.asSecretKey()
    val aead = symmetricKeyset.getPrimitive(Aead::class.java)

    val asymmetricKeyset = data.asPrivateKey()

    return TinkProtoKeysetFormat.serializeEncryptedKeyset(asymmetricKeyset, aead, EMPTY_ASSOCIATED_DATA)
  }

  actual fun decryptAsymmetricKeyBySymmetricKey(data: ByteArray, key: SymmetricKey): AsymmetricKey {
    val symmetricKeyset = key.asSecretKey()
    val aead = symmetricKeyset.getPrimitive(Aead::class.java)

    val asymmetricKeyset = TinkProtoKeysetFormat.parseEncryptedKeyset(data, aead, EMPTY_ASSOCIATED_DATA)

    return asymmetricKeyset.asAsymmetricKey()
  }

  actual fun encryptSymmetricKeyByPublicKey(data: SymmetricKey, key: AsymmetricPublicKey): ByteArray {
    val publicKeyset = key.asPublicKey()
    val hybridEncrypt = publicKeyset.getPrimitive(HybridEncrypt::class.java)

    val symmetricKeyset = data.asSecretKey()
    val serializedEncryptedKeyset = TinkProtoKeysetFormat.serializeKeyset(symmetricKeyset, InsecureSecretKeyAccess.get())

    return hybridEncrypt.encrypt(serializedEncryptedKeyset, EMPTY_ASSOCIATED_DATA)
  }

  actual fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: AsymmetricPrivateKey): SymmetricKey {
    val publicKeyset = key.asPrivateKey()
    val hybridDecrypt = publicKeyset.getPrimitive(HybridDecrypt::class.java)

    val serializedEncryptedKeyset = hybridDecrypt.decrypt(data, EMPTY_ASSOCIATED_DATA)
    val symmetricKeyset = TinkProtoKeysetFormat.parseKeyset(serializedEncryptedKeyset, InsecureSecretKeyAccess.get())

    return symmetricKeyset.asSymmetricKey()
  }
}
