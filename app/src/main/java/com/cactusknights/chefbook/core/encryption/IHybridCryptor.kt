package com.cactusknights.chefbook.core.encryption

import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.mysty.chefbook.core.toByteArray
import com.mysty.chefbook.core.toInt
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
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import org.spongycastle.asn1.DERNull
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.spongycastle.asn1.x509.AlgorithmIdentifier
import org.spongycastle.jcajce.spec.PBKDF2KeySpec

const val AES_SALT_SIZE = 64

interface IHybridCryptor : ICryptor {

    fun encryptKeyPairBySymmetricKey(keyPair: KeyPair, key: SecretKey) : ByteArray
    fun decryptKeyPairBySymmetricKey(data: ByteArray, key: SecretKey) : KeyPair

    fun encryptSymmetricKeyByPrivateKey(data: SecretKey, key: PublicKey) : ByteArray
    fun decryptSymmetricKeyByPrivateKey(data: ByteArray, key: PrivateKey) : SecretKey
}

class HybridCryptor : IHybridCryptor {

    companion object {
        private val IV = "chefbookchefbook".toByteArray()
        private const val TAG_LENGTH = 16

        private const val METADATA_SIZE = Int.SIZE_BYTES

        private const val AES = "AES"
        private const val AES_KEY_LENGTH = 256
        private const val AES_GCM = "AES/GCM/NoPadding"
        private const val AES_SIZE = 256
        private const val AES_ITERATIONS_COUNT = 10000

        private const val RSA = "RSA"
        private const val RSA_LENGTH = 4096

        private val aesGenerator = KeyGenerator.getInstance(AES)
        private val aesCipher = Cipher.getInstance(AES_GCM)
        private val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        private val pfr = AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA256, DERNull.INSTANCE);
        private val spec = GCMParameterSpec(TAG_LENGTH * 8, IV)

        private val rsaGenerator = KeyPairGenerator.getInstance(RSA)
        private val rsaFactory = KeyFactory.getInstance(RSA)
        private val rsaCipher = Cipher.getInstance(RSA)
    }

    private val salt = ByteArray(AES_SALT_SIZE)

    override fun generateKeyPair(): KeyPair {
        rsaGenerator.initialize(RSA_LENGTH)
        return rsaGenerator.generateKeyPair()
    }

    override fun generateSymmetricKey(): SecretKey {
        aesGenerator.init(AES_SIZE)
        return aesGenerator.generateKey()
    }

    override fun generateSymmetricKey(passphrase: String, salt: ByteArray): SecretKey {
        val spec = PBKDF2KeySpec(passphrase.toCharArray(), salt, AES_ITERATIONS_COUNT, AES_KEY_LENGTH, pfr)
        return keyFactory.generateSecret(spec)
    }

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
