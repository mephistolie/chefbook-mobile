package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.common.md5
import com.cactusknights.chefbook.domain.EncryptionRepository
import com.cactusknights.chefbook.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.inject.Inject
import com.cactusknights.chefbook.repositories.local.datasources.LocalEncryptionDataSource
import javax.crypto.spec.SecretKeySpec
import android.util.Log
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteEncryptionDataSource
import com.google.gson.Gson
import java.io.IOException
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.spec.GCMParameterSpec


class SyncEncryptionRepository @Inject constructor(
    private val localSource: LocalEncryptionDataSource,
    private val remoteSource: RemoteEncryptionDataSource
): EncryptionRepository {

    companion object {
        val IV = "chefbookchefbook".toByteArray()
        const val TAG_LENGTH = 16
        const val RSA_LENGTH = 2048
        const val RSA_PUBLIC_KEY_LENGTH = 294
        const val AES = "AES/GCM/NoPadding"
        const val RSA = "RSA"
    }

    private var aesKey : SecretKey? = null

    fun testEncryption() {
        CoroutineScope(Dispatchers.IO).launch {
            setAesKey("test")
            generateRecipeRSA(5)
            val kp = decryptRsa(remoteSource.getEncryptedUserKey()!!)

            val recipe = DecryptedRecipe(
                name = "TEST TEST CUPCAKES",
                ingredients = arrayListOf(MarkdownString("FIRST INGREDIENT"), MarkdownString("SECOND INGREDIENT", MarkdownTypes.HEADER)),
                cooking = arrayListOf(MarkdownString("FIRST STEP"), MarkdownString("SECOND STEP", MarkdownTypes.HEADER))
            )
            Log.e("ENCRYPTION", Gson().toJson(recipe).toString())
            val encryptedRecipe = recipe.encrypt(kp.public)
            Log.e("ENCRYPTION", Gson().toJson(encryptedRecipe).toString())
            val decryptedRecipe = encryptedRecipe.decrypt(kp.private)
            Log.e("ENCRYPTION", Gson().toJson(decryptedRecipe).toString())
        }
    }

    override suspend fun getRecipe(recipeId: Int) {
    }

    override suspend fun setAesKey(keyphrase: String) {
        aesKey = SecretKeySpec(keyphrase.md5().toByteArray(), AES)
    }

    override suspend fun generateRecipeRSA(recipeId: Int) {
        val kpg = KeyPairGenerator.getInstance(RSA)
        kpg.initialize(RSA_LENGTH)
        val kp = kpg.genKeyPair()

        val stored = remoteSource.setEncryptedUserKey(encryptRsa(kp))
        if (!stored) throw IOException()
    }

    override suspend fun setRecipeRsaPrivateKey(recipeId: Int) {
        TODO("Not yet implemented")
    }

    private fun encryptRsa(rsa: KeyPair) : ByteArray {
        val currentAesKey = aesKey ?: throw IOException()

        val keys = rsa.public.encoded + rsa.private.encoded

        val aesCipher = Cipher.getInstance(AES)
        val spec = GCMParameterSpec(TAG_LENGTH * 8, IV)
        aesCipher.init(Cipher.ENCRYPT_MODE, currentAesKey, spec)
        return aesCipher.doFinal(keys)
    }

    private fun decryptRsa(encryptedRsa: ByteArray) : KeyPair {
        val currentAesKey = aesKey ?: throw IOException()

        val aesCipher = Cipher.getInstance(AES)
        val spec = GCMParameterSpec(TAG_LENGTH * 8, IV)
        aesCipher.init(Cipher.DECRYPT_MODE, currentAesKey, spec)
        val rsa = aesCipher.doFinal(encryptedRsa)

        val publicKeyBytes = rsa.copyOfRange(0, RSA_PUBLIC_KEY_LENGTH)
        val publicKey = KeyFactory.getInstance(RSA).generatePublic(X509EncodedKeySpec(publicKeyBytes))

        val privateKeyBytes = rsa.copyOfRange(RSA_PUBLIC_KEY_LENGTH, rsa.size)
        val privateKey = KeyFactory.getInstance(RSA).generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes))

        return KeyPair(publicKey, privateKey)
    }
}