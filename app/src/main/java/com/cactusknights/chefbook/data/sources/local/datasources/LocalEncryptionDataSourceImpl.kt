package com.cactusknights.chefbook.data.sources.local.datasources

import android.content.Context
import com.cactusknights.chefbook.data.EncryptionDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEncryptionDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : EncryptionDataSource {

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
        val recipeKeyFile = File(context.filesDir, "$RECIPES_DIR$recipeId/$RECIPE_KEY_FILE")
        if (!recipeKeyFile.exists()) return null
        return recipeKeyFile.readBytes()
    }

    override suspend fun setEncryptedRecipeKey(recipeId: Int, key: ByteArray) {
        val recipeDir = File(context.filesDir, RECIPES_DIR + recipeId.toString())
        if (!recipeDir.exists()) recipeDir.mkdirs()
        val recipeKeyFile = File(recipeDir, RECIPE_KEY_FILE)
        recipeKeyFile.writeBytes(key)
    }

    override suspend fun deleteEncryptedRecipeKey(recipeId: Int) {
        val recipeDir = File(context.filesDir, RECIPES_DIR + recipeId.toString())
        if (!recipeDir.exists()) recipeDir.mkdirs()
        val recipeKeyFile = File(recipeDir, RECIPE_KEY_FILE)
        if (recipeKeyFile.exists()) {
            val deleted = recipeKeyFile.delete()
            if (!deleted) throw throw Exception("can't delete local key for recipe $recipeId")
        }
    }

    companion object {
        private const val RECIPES_DIR = "recipes/"
        private const val RECIPE_KEY_FILE = "recipe_key"
        private const val USER_KEY_FILE = "user_key"
    }
}