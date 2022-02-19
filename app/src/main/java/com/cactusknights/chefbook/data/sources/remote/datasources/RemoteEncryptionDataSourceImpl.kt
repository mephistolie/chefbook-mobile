package com.cactusknights.chefbook.data.sources.remote.datasources

import com.cactusknights.chefbook.core.retrofit.UriDataProviderImpl
import com.cactusknights.chefbook.data.EncryptionDataSource
import com.cactusknights.chefbook.data.sources.remote.api.EncryptionApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

class RemoteEncryptionDataSourceImpl @Inject constructor(
    private val api: EncryptionApi,
    private val fileManager : UriDataProviderImpl
) : EncryptionDataSource {

    override suspend fun getEncryptedUserKey(): ByteArray? {
        val response = api.getUserKeyLink()
        if (response.code() != 200 || response.body() == null) return null
        val link = response.body()!!.link
        return try {
            fileManager.getData(link)
        } catch (e: java.lang.Exception) {
            null
        }
    }

    override suspend fun setEncryptedUserKey(data: ByteArray) {
        val file =  data.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        api.uploadUserKey(MultipartBody.Part.createFormData(KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file))
    }

    override suspend fun deleteEncryptedUserKey() {
        val response = api.deleteUserKey()
        if (response.code() != 200) throw Exception("can't delete remote user key")
    }

    override suspend fun getEncryptedRecipeKey(recipeId: Int): ByteArray? {
        val response = api.getRecipeKeyLink(recipeId.toString())
        if (response.code() != 200 || response.body() == null) return null
        val link = response.body()!!.link
        return try {
            fileManager.getData(link)
        } catch (e: java.lang.Exception) {
            null
        }
    }

    override suspend fun setEncryptedRecipeKey(recipeId: Int, key: ByteArray) {
        val file =  key.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        api.uploadRecipeKey(recipeId.toString(), MultipartBody.Part.createFormData(KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file))
    }

    override suspend fun deleteEncryptedRecipeKey(recipeId: Int) {
        val response = api.deleteRecipeKey(recipeId.toString())
        if (response.code() != 200) throw Exception("can't delete remote key for recipe $recipeId")
    }

    companion object {
        private const val KEY_CONTENT_TYPE = "application/octet-stream"
        private const val KEY_FORM_DATA_NAME = "file"
        private const val KEY_FIELD_NAME = "file"
    }
}