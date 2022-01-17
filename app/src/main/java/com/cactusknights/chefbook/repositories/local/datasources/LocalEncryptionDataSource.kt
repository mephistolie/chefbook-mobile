package com.cactusknights.chefbook.repositories.local.datasources

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.cactusknights.chefbook.base.Constants
import com.cactusknights.chefbook.domain.EncryptionDataStore
import com.cactusknights.chefbook.domain.UserDataSource
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.sync.SyncEncryptionRepository
import com.cactusknights.chefbook.repositories.sync.SyncEncryptionRepository.Companion.RSA_PUBLIC_KEY_LENGTH
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class LocalEncryptionDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : EncryptionDataStore {

    companion object {
        private const val RECIPES_DIR = "recipes/"
        private const val KEY_FILE = "key"
        private const val USER_KEY_FILE = "user_key"
    }

    init {
        val recipesDir = File(context.filesDir, RECIPES_DIR)
        if (!recipesDir.exists()) recipesDir.mkdir()
    }

    override suspend fun getEncryptedUserKey(): ByteArray? {
        val rsaFile = File(context.filesDir, USER_KEY_FILE)
        if (!rsaFile.exists()) return null
        return rsaFile.readBytes()
    }

    override suspend fun setEncryptedUserKey(encryptedRsa: ByteArray): Boolean {
        return try {
            val rsaFile = File(context.filesDir, USER_KEY_FILE)
            rsaFile.writeBytes(encryptedRsa)
            true
        } catch (e: Exception) { false }
    }

    override suspend fun getEncryptedRecipeKeys(recipeId: Int): ByteArray? {
        val rsaFile = File(context.filesDir, "$RECIPES_DIR$recipeId/$KEY_FILE")
        if (!rsaFile.exists()) return null
        return rsaFile.readBytes()
    }

    override suspend fun setEncryptedRecipeKeys(recipeId: Int, encryptedRsa: ByteArray): Boolean {
        return try {
            val recipeDir = File(context.filesDir, RECIPES_DIR + recipeId.toString())
            if (!recipeDir.exists()) recipeDir.mkdir()
            val rsaFile = File(recipeDir, KEY_FILE)
            rsaFile.writeBytes(encryptedRsa)
            true
        } catch (e: Exception) {
            false
        }
    }
}