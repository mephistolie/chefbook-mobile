package com.cactusknights.chefbook.data.sources.remote.datasources

import com.cactusknights.chefbook.models.Profile
import com.cactusknights.chefbook.data.RemoteProfileDataSource
import com.cactusknights.chefbook.data.sources.remote.api.ProfileApi
import com.cactusknights.chefbook.data.sources.remote.dto.UsernameInputDto
import com.cactusknights.chefbook.data.sources.remote.dto.toUser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class RemoteProfileDataSourceImpl @Inject constructor(
    private val api: ProfileApi
) : RemoteProfileDataSource {

    companion object {
        private const val AVATAR_CONTENT_TYPE = "image/jpeg"
        private const val AVATAR_FORM_DATA_NAME = "file"
        private const val AVATAR_FIELD_NAME = "file"
    }

    override suspend fun getProfileInfo(): Profile {
        val response = api.getUserInfo()
        return response.body()!!.toUser()
    }

    override suspend fun changeName(username: String) {
        api.changeName(UsernameInputDto(username))
    }

    override suspend fun uploadAvatar(uriString: String) {
        val avatar = File(uriString)
        val file = avatar.asRequestBody(contentType = AVATAR_CONTENT_TYPE.toMediaTypeOrNull())
        api.uploadAvatar(MultipartBody.Part.createFormData(AVATAR_FORM_DATA_NAME, AVATAR_FIELD_NAME, file))
    }

    override suspend fun deleteAvatar() {
        api.deleteAvatar()
    }
}