package com.cactusknights.chefbook.repositories.local.datasources

import android.content.Context
import com.cactusknights.chefbook.repositories.EncryptionDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
        val userKeyFile = File(context.filesDir, USER_KEY_FILE)
        if (!userKeyFile.exists()) return null
        return userKeyFile.readBytes()
    }

    override suspend fun setEncryptedUserKey(data: ByteArray) {
        val userKeyFile = File(context.filesDir, USER_KEY_FILE)
        userKeyFile.writeBytes(data)
    }

    override suspend fun deleteEncryptedUserKey() {
        val userKeyFile = File(context.filesDir, USER_KEY_FILE)
        if (userKeyFile.exists()) {
            val deleted = userKeyFile.delete()
            if (!deleted) throw throw Exception("can't delete local user key")
        }
    }

    override suspend fun getEncryptedRecipeKey(recipeId: Int): ByteArray? {
        val userKeyFile = File(context.filesDir, "$RECIPES_DIR$recipeId/$KEY_FILE")
        if (!userKeyFile.exists()) return null
        return userKeyFile.readBytes()
    }

    override suspend fun setEncryptedRecipeKey(recipeId: Int, key: ByteArray) {
        val recipeDir = File(context.filesDir, RECIPES_DIR + recipeId.toString())
        if (!recipeDir.exists()) recipeDir.mkdir()
        val userKeyFile = File(recipeDir, KEY_FILE)
        userKeyFile.writeBytes(key)
    }

    override suspend fun deleteEncryptedRecipeKey(recipeId: Int) {
        val recipeDir = File(context.filesDir, RECIPES_DIR + recipeId.toString())
        if (!recipeDir.exists()) recipeDir.mkdir()
        val userKeyFile = File(recipeDir, KEY_FILE)
        if (userKeyFile.exists()) {
            val deleted = userKeyFile.delete()
            if (!deleted) throw throw Exception("can't delete local key for recipe $recipeId")
        }
    }
}