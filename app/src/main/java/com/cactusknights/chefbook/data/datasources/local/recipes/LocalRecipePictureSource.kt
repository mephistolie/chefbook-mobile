package com.cactusknights.chefbook.data.datasources.local.recipes

import android.content.Context
import android.net.Uri
import com.cactusknights.chefbook.core.LocalPaths.PICTURES_DIR
import com.cactusknights.chefbook.core.LocalPaths.RECIPES_DIR
import com.cactusknights.chefbook.data.IRecipePictureSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.FileError
import com.cactusknights.chefbook.domain.entities.action.FileErrorType
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipePictureSource @Inject constructor(
    @ApplicationContext private val context: Context
) : IRecipePictureSource {

    override suspend fun getPictures(recipeId: Int): ActionStatus<List<String>> {
        val picturesDir = File(context.filesDir, "$RECIPES_DIR/$recipeId/$PICTURES_DIR")
        val pictures = picturesDir.listFiles()?: arrayOf()
        return DataResult(pictures.map { Uri.fromFile(it).toString() })
    }

    override suspend fun addPicture(recipeId: Int, data: ByteArray): ActionStatus<String> {
        return try {
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

    override suspend fun deletePicture(recipeId : Int, name: String): SimpleAction {
        return try {
            val picture = File(context.filesDir, "$RECIPES_DIR/$recipeId/$PICTURES_DIR/$name")
            if (!picture.delete()) Failure(FileError(FileErrorType.UNABLE_MODIFY))
            SuccessResult
        } catch (e: Exception) {
            Failure(FileError(FileErrorType.UNKNOWN_ERROR))
        }
    }

}