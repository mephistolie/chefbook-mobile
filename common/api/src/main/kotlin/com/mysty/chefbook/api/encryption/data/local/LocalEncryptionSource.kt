package com.mysty.chefbook.api.encryption.data.local

import android.content.Context
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.errors.FileError
import com.mysty.chefbook.api.common.communication.errors.FileErrorType
import com.mysty.chefbook.api.common.constants.LocalPaths.RECIPES_DIR
import com.mysty.chefbook.api.common.constants.LocalPaths.RECIPE_KEY_FILE
import com.mysty.chefbook.api.common.constants.LocalPaths.USER_KEY_FILE
import com.mysty.chefbook.api.encryption.data.ILocalEncryptionSource
import java.io.File
import java.io.IOException

internal class LocalEncryptionSource(
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
