package com.cactusknights.chefbook.repositories.remote.datasources

import com.cactusknights.chefbook.domain.EncryptionDataStore
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RemoteEncryptionDataSource @Inject constructor(
    private val api: ChefBookApi
) : EncryptionDataStore {

    override suspend fun getEncryptedUserKey(): ByteArray? {
        val response = api.getUserKeyLink()
        if (response.code() != 200 || response.body() == null) return null
        val link = response.body()!!.link
        val fileResponse = api.getFile(link)
        if (fileResponse.code() != 200 || fileResponse.body() == null) return null
        return fileResponse.body()!!.byteStream().readBytes()
    }

    override suspend fun setEncryptedUserKey(encryptedRsa: ByteArray): Boolean {
        val file =  encryptedRsa.toRequestBody("application/octet-stream".toMediaTypeOrNull())
        val response = api.uploadUserKey(MultipartBody.Part.createFormData("file", "file", file))
        return response.code() == 200
    }

    override suspend fun getEncryptedRecipeKeys(recipeId: Int): ByteArray? {
        val response = api.getRecipeKeyLink(recipeId.toString())
        if (response.code() != 200 || response.body() == null) return null
        val link = response.body()!!.link
        val fileResponse = api.getFile(link)
        if (fileResponse.code() != 200 || fileResponse.body() == null) return null
        return fileResponse.body()!!.byteStream().readBytes()
    }

    override suspend fun setEncryptedRecipeKeys(recipeId: Int, encryptedRsa: ByteArray): Boolean {
        val file =  encryptedRsa.toRequestBody("application/octet-stream".toMediaTypeOrNull())
        val response = api.uploadRecipeKey(recipeId.toString(), MultipartBody.Part.createFormData("file", "file", file))
        return response.code() == 200
    }
}