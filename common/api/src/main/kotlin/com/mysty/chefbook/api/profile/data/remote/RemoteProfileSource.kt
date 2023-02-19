package com.mysty.chefbook.api.profile.data.remote

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import com.mysty.chefbook.api.profile.data.IRemoteProfileSource
import com.mysty.chefbook.api.profile.data.remote.api.ProfileApi
import com.mysty.chefbook.api.profile.data.remote.dto.ChangePasswordRequest
import com.mysty.chefbook.api.profile.data.remote.dto.ChangeUsernameRequest
import com.mysty.chefbook.api.profile.domain.entities.Profile
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

internal class RemoteProfileSource(
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
