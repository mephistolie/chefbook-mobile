package com.mysty.chefbook.api.encryption.data.remote

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.errors.ServerError
import com.mysty.chefbook.api.common.communication.errors.ServerErrorType
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import com.mysty.chefbook.api.encryption.data.IRemoteEncryptionSource
import com.mysty.chefbook.api.encryption.data.remote.api.EncryptionApi
import com.mysty.chefbook.api.files.data.IFileSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

internal class RemoteEncryptionSource(
    private val api: EncryptionApi,
    private val fileSource : IFileSource,
    private val handleResponse: INetworkHandler,
) : IRemoteEncryptionSource {

    override suspend fun getUserKeyLink(): ActionStatus<String> {
        val result = handleResponse { api.getUserKeyLink() }
        if (result.isFailure()) return result.toActionStatus().asFailure()
        return DataResult(result.body().link)
    }

    override suspend fun getUserKey(link: String): ActionStatus<ByteArray> {
        val keyResult = fileSource.getFile(link)
        return if (keyResult.isSuccess()) DataResult(keyResult.data()) else Failure(ServerError(ServerErrorType.NOT_FOUND))
    }

    override suspend fun setUserKey(data: ByteArray): SimpleAction {
        val file =  data.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        return handleResponse { api.uploadUserKey(MultipartBody.Part.createFormData(
            KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file)) }.toActionStatus().asEmpty()
    }

    override suspend fun deleteUserKey(): SimpleAction =
        handleResponse { api.deleteUserKey() }.toActionStatus().asEmpty()

    override suspend fun getRecipeKey(recipeId: String): ActionStatus<ByteArray> {
        val result = handleResponse { api.getRecipeKeyLink(recipeId) }
        if (result.isFailure()) return result.toActionStatus().asFailure()
        val link = result.body().link
        val keyResult = fileSource.getFile(link)
        return if (keyResult.isSuccess()) DataResult(keyResult.data()) else Failure(ServerError(ServerErrorType.NOT_FOUND))
    }

    override suspend fun setRecipeKey(recipeId: String, key: ByteArray): SimpleAction {
        val file = key.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        return handleResponse { api.uploadRecipeKey(recipeId, MultipartBody.Part.createFormData(
            KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file)) }.toActionStatus().asEmpty()
    }

    override suspend fun deleteRecipeKey(recipeId: String): SimpleAction =
        handleResponse { api.deleteRecipeKey(recipeId) }.toActionStatus().asEmpty()

    companion object {
        private const val KEY_CONTENT_TYPE = "application/octet-stream"
        private const val KEY_FORM_DATA_NAME = "file"
        private const val KEY_FIELD_NAME = "file"
    }

}