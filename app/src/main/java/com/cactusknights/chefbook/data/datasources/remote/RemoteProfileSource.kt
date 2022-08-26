package com.cactusknights.chefbook.data.datasources.remote

import com.cactusknights.chefbook.data.IRemoteProfileSource
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.dto.remote.profile.ChangePasswordRequest
import com.cactusknights.chefbook.data.dto.remote.profile.ChangeUsernameRequest
import com.cactusknights.chefbook.data.dto.remote.profile.toEntity
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.ProfileApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.profile.Profile
import java.io.File
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class RemoteProfileSource @Inject constructor(
    private val api: ProfileApi,
    private val handleResponse: INetworkHandler
) : IRemoteProfileSource {

    companion object {
        private const val AVATAR_CONTENT_TYPE = "image/jpeg"
        private const val AVATAR_FORM_DATA_NAME = "file"
        private const val AVATAR_FIELD_NAME = "file"
    }

    override suspend fun getProfileInfo(): ActionStatus<Profile> {
        val result = handleResponse { api.getUserInfo() }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().toEntity())
    }

    override suspend fun changeName(username: String): SimpleAction =
        handleResponse { api.changeName(ChangeUsernameRequest(username)) }.toActionStatus().asEmpty()

    override suspend fun changePassword(oldPassword: String, newPassword: String): SimpleAction =
        handleResponse { api.changePassword(ChangePasswordRequest(oldPassword, newPassword)) }
            .toActionStatus().asEmpty()

    override suspend fun uploadAvatar(uriString: String): SimpleAction {
        val avatar = File(uriString)
        val file = avatar.asRequestBody(contentType = AVATAR_CONTENT_TYPE.toMediaTypeOrNull())
        return api.uploadAvatar(MultipartBody.Part.createFormData(AVATAR_FORM_DATA_NAME, AVATAR_FIELD_NAME, file))
            .toActionStatus().asEmpty()
    }

    override suspend fun deleteAvatar(): SimpleAction =
        api.deleteAvatar().toActionStatus().asEmpty()
}
