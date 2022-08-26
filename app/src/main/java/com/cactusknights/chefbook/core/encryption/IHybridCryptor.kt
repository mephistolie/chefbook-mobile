package com.cactusknights.chefbook.core.encryption

import com.cactusknights.chefbook.common.sha256
import com.cactusknights.chefbook.common.toByteArray
import com.cactusknights.chefbook.common.toInt
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

interface IHybridCryptor : ICryptor {

    fun encryptKeyPairBySymmetricKey(keyPair: KeyPair, key: SecretKey) : ByteArray
    fun decryptKeyPairBySymmetricKey(data: ByteArray, key: SecretKey) : KeyPair

    fun encryptSymmetricKeyByPrivateKey(data: SecretKey, key: PublicKey) : ByteArray
    fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: PrivateKey) : SecretKey
}

class HybridCryptor : IHybridCryptor {

    companion object {
        val IV = "chefbookchefbook".toByteArray()
        const val TAG_LENGTH = 16

        const val METADATA_SIZE = Int.SIZE_BYTES

        const val AES = "AES"
        const val AES_KEY_LENGTH = 32
        const val AES_GCM = "AES/GCM/NoPadding"
        const val AES_SIZE = 256

        const val RSA = "RSA"
        const val RSA_LENGTH = 4096
    }

    private val rsaGenerator = KeyPairGenerator.getInstance(RSA)
    private val rsaFactory = KeyFactory.getInstance(RSA)
    private val rsaCipher = Cipher.getInstance(RSA)

    private val aesGenerator = KeyGenerator.getInstance(AES)
    private val aesCipher = Cipher.getInstance(AES_GCM)
    private val spec = GCMParameterSpec(TAG_LENGTH * 8, IV)

    override fun generateKeyPair(): KeyPair {
        rsaGenerator.initialize(RSA_LENGTH)
        return rsaGenerator.generateKeyPair()
    }

    override fun generateSymmetricKey(): SecretKey {
        aesGenerator.init(AES_SIZE)
        return aesGenerator.generateKey()
    }

    override fun generateSymmetricKey(passphrase: String) = SecretKeySpec(passphrase.sha256.substring(0, AES_KEY_LENGTH).toByteArray(), AES_GCM)

    override fun encryptDataBySymmetricKey(data: ByteArray, key: SecretKey) : ByteArray {
        aesCipher.init(Cipher.ENCRYPT_MODE, key, spec)
        return aesCipher.doFinal(data)
    }

    override fun decryptDataBySymmetricKey(data: ByteArray, key: SecretKey) : ByteArray {
        aesCipher.init(Cipher.DECRYPT_MODE, key, spec)
        return aesCipher.doFinal(data)
    }

    override fun encryptDataByAsymmetricKey(data: ByteArray, key: PublicKey): ByteArray {
        rsaCipher.init(Cipher.ENCRYPT_MODE, key)
        return rsaCipher.doFinal(data)
    }

    override fun decryptDataByAsymmetricKey(data: ByteArray, key: PrivateKey): ByteArray {
        rsaCipher.init(Cipher.DECRYPT_MODE, key)
        return rsaCipher.doFinal(data)
    }

    override fun encryptKeyPairBySymmetricKey(keyPair: KeyPair, key: SecretKey): ByteArray {
        val encryptedPublicKey = encryptDataBySymmetricKey(keyPair.public.encoded, key)
        val encryptedPrivateKey = encryptDataBySymmetricKey(keyPair.private.encoded, key)
        val publicKeySize : Int = encryptedPublicKey.size
        return publicKeySize.toByteArray() + encryptedPublicKey + encryptedPrivateKey
    }

    override fun decryptKeyPairBySymmetricKey(data: ByteArray, key: SecretKey): KeyPair {
        val publicKeySize = data.copyOfRange(0,  Int.SIZE_BYTES).toInt()
        val encryptedPublicKey = data.copyOfRange(Int.SIZE_BYTES, METADATA_SIZE + publicKeySize)
        val encryptedPrivateKey = data.copyOfRange(METADATA_SIZE + publicKeySize, data.size)

        aesCipher.init(Cipher.DECRYPT_MODE, key, spec)
        val publicKeyBytes = aesCipher.doFinal(encryptedPublicKey)
        aesCipher.init(Cipher.DECRYPT_MODE, key, spec)
        val privateKeyBytes = aesCipher.doFinal(encryptedPrivateKey)

        val privateKey = rsaFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes))
        val publicKey = rsaFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes))

        return KeyPair(publicKey, privateKey)
    }

    override fun encryptSymmetricKeyByPrivateKey(data: SecretKey, key: PublicKey): ByteArray {
        return encryptDataByAsymmetricKey(data.encoded, key)
    }

    override fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: PrivateKey): SecretKey {
        val symmetricKey = decryptDataByAsymmetricKey(data, key)
        return SecretKeySpec(symmetricKey, AES_GCM)
    }
}