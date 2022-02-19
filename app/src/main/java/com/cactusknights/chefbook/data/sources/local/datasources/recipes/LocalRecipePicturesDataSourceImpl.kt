package com.cactusknights.chefbook.data.sources.local.datasources.recipes

import android.content.Context
import android.net.Uri
import com.cactusknights.chefbook.data.RecipePicturesDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipePicturesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RecipePicturesDataSource {

    override suspend fun getPicturesUri(recipeId: Int): List<String> {
        val picturesDir = File(context.filesDir, RECIPES_DIR + recipeId.toString() + PICTURES_DIR)
        val pictures = picturesDir.listFiles()?: return listOf()
        return pictures.map { Uri.fromFile(it).toString() }
    }

    override suspend fun addPicture(recipeId: Int, data: ByteArray): String {
        val picturesDir = File(context.filesDir, RECIPES_DIR + recipeId.toString() + PICTURES_DIR)
        val name = UUID.randomUUID().toString()
        if (!picturesDir.exists()) picturesDir.mkdirs()
        val recipePicture = File(picturesDir, name)
        recipePicture.writeBytes(data)
        return recipePicture.canonicalPath
    }

    override suspend fun deletePicture(recipeId : Int, name: String) {
        val picture = File(context.filesDir, RECIPES_DIR + recipeId.toString() + PICTURES_DIR + name)
        picture.delete()
    }

    companion object {
        private const val RECIPES_DIR = "recipes/"
        private const val PICTURES_DIR = "pictures/"
    }
}