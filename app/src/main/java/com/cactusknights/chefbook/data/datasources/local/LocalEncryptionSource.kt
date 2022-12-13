package com.cactusknights.chefbook.data.datasources.local

import android.content.Context
import com.cactusknights.chefbook.core.LocalPaths.RECIPES_DIR
import com.cactusknights.chefbook.core.LocalPaths.RECIPE_KEY_FILE
import com.cactusknights.chefbook.core.LocalPaths.USER_KEY_FILE
import com.cactusknights.chefbook.data.ILocalEncryptionSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.FileError
import com.cactusknights.chefbook.domain.entities.action.FileErrorType
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import java.io.File
import java.io.IOException
import javax.inject.Singleton

@Singleton
class LocalEncryptionSource(
    private val context: Context
) : ILocalEncryptionSource {

    override suspend fun getUserKey(): ActionStatus<ByteArray> {
        val userKeyFile = File(context.filesDir, USER_KEY_FILE)
        if (!userKeyFile.exists()) return Failure(FileError(FileErrorType.NOT_EXISTS))
        return DataResult(userKeyFile.readBytes())
    }

    override suspend fun setUserKey(data: ByteArray): SimpleAction {
        return try {
            val userKeyFile = File(context.filesDir, USER_KEY_FILE)
            userKeyFile.writeBytes(data)
            SuccessResult
        } catch (e: IOException) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

    override suspend fun deleteUserKey(): SimpleAction {
        return try {
            val userKeyFile = File(context.filesDir, USER_KEY_FILE)
            if (userKeyFile.exists()) {
                val deleted = userKeyFile.delete()
                if (!deleted) Failure(FileError(FileErrorType.UNABLE_MODIFY))
            }
            SuccessResult
        } catch (e: IOException) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

    override suspend fun getRecipeKey(recipeId: String): ActionStatus<ByteArray> {
        val recipeKeyFile = File(context.filesDir, "$RECIPES_DIR/$recipeId/$RECIPE_KEY_FILE")
        if (!recipeKeyFile.exists()) return Failure(FileError(FileErrorType.NOT_EXISTS))
        return DataResult(recipeKeyFile.readBytes())
    }

    override suspend fun setRecipeKey(recipeId: String, key: ByteArray): SimpleAction {
        return try {
            val recipeDir = File(context.filesDir, "$RECIPES_DIR/$recipeId")
            if (!recipeDir.exists()) recipeDir.mkdirs()
            val recipeKeyFile = File(recipeDir, RECIPE_KEY_FILE)
            recipeKeyFile.writeBytes(key)
            SuccessResult
        } catch (e: IOException) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

    override suspend fun deleteRecipeKey(recipeId: String): SimpleAction {
        return try {
            val recipeDir = File(context.filesDir, "$RECIPES_DIR/$recipeId")
            if (!recipeDir.exists()) SuccessResult
            val recipeKeyFile = File(recipeDir, RECIPE_KEY_FILE)
            if (recipeKeyFile.exists()) {
                if (!recipeKeyFile.delete()) Failure(FileError(FileErrorType.UNABLE_MODIFY))
            }
            SuccessResult
        } catch (e: IOException) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

}
