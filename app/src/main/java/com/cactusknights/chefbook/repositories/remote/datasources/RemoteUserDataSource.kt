package com.cactusknights.chefbook.repositories.remote.datasources

import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.ServerUserDataSource
import com.cactusknights.chefbook.repositories.remote.api.UsersApi
import com.cactusknights.chefbook.repositories.remote.dto.UsernameInputDto
import com.cactusknights.chefbook.repositories.remote.dto.toUser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUserDataSource @Inject constructor(
    private val api: UsersApi
) : ServerUserDataSource {

    companion object {
        private const val AVATAR_CONTENT_TYPE = "image/jpeg"
        private const val AVATAR_FORM_DATA_NAME = "file"
        private const val AVATAR_FIELD_NAME = "file"
    }

    override suspend fun getUserInfo(): User {
        val response = api.getUserInfo()
        return response.body()!!.toUser()
    }

    override suspend fun changeName(username: String) {
        api.changeName(UsernameInputDto(username))
    }

    override suspend fun uploadAvatar(uri: String) {
        val avatar = File(uri)
        val file = avatar.asRequestBody(contentType = AVATAR_CONTENT_TYPE.toMediaTypeOrNull())
        api.uploadAvatar(MultipartBody.Part.createFormData(AVATAR_FORM_DATA_NAME, AVATAR_FIELD_NAME, file))
    }

    override suspend fun deleteAvatar() {
        api.deleteAvatar()
    }
}