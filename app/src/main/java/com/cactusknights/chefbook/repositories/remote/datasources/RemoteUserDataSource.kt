package com.cactusknights.chefbook.repositories.remote.datasources

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.cactusknights.chefbook.domain.ServerUserDataSource
import com.cactusknights.chefbook.domain.UserDataSource
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.UsernameInputDto
import com.cactusknights.chefbook.repositories.remote.dto.toUser
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URI
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(
    private val api: ChefBookApi
) : ServerUserDataSource {

    override suspend fun getUserInfo(): User {
        val response = api.getUserInfo()
        return response.body()!!.toUser()
    }

    override suspend fun changeName(username: String) {
        api.changeName(UsernameInputDto(username))
    }

    override suspend fun uploadAvatar(uri: String) {
        val avatar = File(uri)
        val file = avatar.asRequestBody(contentType = "image/jpeg".toMediaTypeOrNull())
        api.uploadAvatar(MultipartBody.Part.createFormData("file", "file", file))
    }

    override suspend fun deleteAvatar() {
        api.deleteAvatar()
    }
}