package com.mysty.chefbook.api.recipe.data.local

import android.content.Context
import android.net.Uri
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.errors.FileError
import com.mysty.chefbook.api.common.communication.errors.FileErrorType
import com.mysty.chefbook.api.common.constants.LocalPaths.PICTURES_DIR
import com.mysty.chefbook.api.common.constants.LocalPaths.RECIPES_DIR
import com.mysty.chefbook.api.recipe.data.repositories.IRecipePictureSource
import com.mysty.chefbook.core.coroutines.AppDispatchers
import java.io.File
import java.util.UUID
import kotlinx.coroutines.withContext

internal class LocalRecipePictureSource(
    private val context: Context,
    val dispatchers: AppDispatchers,
) : IRecipePictureSource {

    override suspend fun getPictures(recipeId: String): ActionStatus<List<String>> = withContext(dispatchers.io) {
        val picturesDir = File(context.filesDir, "$RECIPES_DIR/$recipeId/$PICTURES_DIR")
        val pictures = picturesDir.listFiles()?: arrayOf()
        return@withContext DataResult(pictures.map { Uri.fromFile(it).toString() })
    }

    override suspend fun addPicture(recipeId: String, data: ByteArray): ActionStatus<String> = withContext(dispatchers.io) {
        return@withContext try {
            val picturesDir = File(context.filesDir, "$RECIPES_DIR/$recipeId/$PICTURES_DIR")
            val name = UUID.randomUUID().toString()
            if (!picturesDir.exists()) picturesDir.mkdirs()
            val recipePicture = File(picturesDir, name)
            recipePicture.writeBytes(data)
            DataResult(recipePicture.canonicalPath)
        } catch (e: Exception) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

    override suspend fun deletePicture(recipeId : String, name: String): SimpleAction = withContext(dispatchers.io) {
        return@withContext try {
            val picture = File(context.filesDir, "$RECIPES_DIR/$recipeId/$PICTURES_DIR/$name")
            if (!picture.delete()) Failure(FileError(FileErrorType.UNABLE_MODIFY))
            SuccessResult
        } catch (e: Exception) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

}
