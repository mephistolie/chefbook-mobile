package com.cactusknights.chefbook.data.sources.remote.datasources.recipes

import com.cactusknights.chefbook.data.RecipePicturesDataSource
import com.cactusknights.chefbook.data.sources.remote.api.RecipesApi
import com.cactusknights.chefbook.data.sources.remote.dto.DeleteRecipePictureInput
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

class RemoteRecipePicturesDataSourceImpl @Inject constructor(
    private val api: RecipesApi
) : RecipePicturesDataSource {

    override suspend fun getPicturesUri(recipeId: Int): List<String> {
        return api.getRecipePictures(recipeId.toString()).body()?: listOf()
    }

    override suspend fun addPicture(recipeId: Int, data: ByteArray): String {
        val file =  data.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        val response = api.uploadRecipePicture(recipeId.toString(), MultipartBody.Part.createFormData(
            KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file))
        return response.body()!!.link
    }

    override suspend fun deletePicture(recipeId : Int, name: String) {
        api.deleteRecipePicture(recipeId.toString(), name)
    }

    companion object {
        private const val KEY_CONTENT_TYPE = "image/jpeg"
        private const val KEY_FORM_DATA_NAME = "file"
        private const val KEY_FIELD_NAME = "file"
    }
}