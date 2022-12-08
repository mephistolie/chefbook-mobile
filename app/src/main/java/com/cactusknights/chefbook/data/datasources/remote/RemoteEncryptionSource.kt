package com.cactusknights.chefbook.data.datasources.remote

import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.data.IRemoteEncryptionSource
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.EncryptionApi
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.ServerError
import com.cactusknights.chefbook.domain.entities.action.ServerErrorType
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class RemoteEncryptionSource @Inject constructor(
    private val api: EncryptionApi,
    @Remote
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

    override suspend fun getRecipeKey(recipeId: Int): ActionStatus<ByteArray> {
        val result = handleResponse { api.getRecipeKeyLink(recipeId) }
        if (result.isFailure()) return result.toActionStatus().asFailure()
        val link = result.body().link
        val keyResult = fileSource.getFile(link)
        return if (keyResult.isSuccess()) DataResult(keyResult.data()) else Failure(ServerError(ServerErrorType.NOT_FOUND))
    }

    override suspend fun setRecipeKey(recipeId: Int, key: ByteArray): SimpleAction {
        val file = key.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        return handleResponse { api.uploadRecipeKey(recipeId, MultipartBody.Part.createFormData(
            KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file)) }.toActionStatus().asEmpty()
    }

    override suspend fun deleteRecipeKey(recipeId: Int): SimpleAction =
        handleResponse { api.deleteRecipeKey(recipeId) }.toActionStatus().asEmpty()

    companion object {
        private const val KEY_CONTENT_TYPE = "application/octet-stream"
        private const val KEY_FORM_DATA_NAME = "file"
        private const val KEY_FIELD_NAME = "file"
    }

}