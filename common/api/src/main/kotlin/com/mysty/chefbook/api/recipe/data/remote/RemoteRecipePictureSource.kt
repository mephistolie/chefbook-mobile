package com.mysty.chefbook.api.recipe.data.remote

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import com.mysty.chefbook.api.recipe.data.remote.api.RecipeApi
import com.mysty.chefbook.api.recipe.data.repositories.IRecipePictureSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

internal class RemoteRecipePictureSource(
    private val api: RecipeApi,
    private val handleResponse: INetworkHandler,
) : IRecipePictureSource {

    override suspend fun getPictures(recipeId: String): ActionStatus<List<String>> =
        handleResponse { api.getRecipePictures(recipeId) }.toActionStatus()

    override suspend fun addPicture(recipeId: String, data: ByteArray): ActionStatus<String> {
        val file =  data.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        val result =  handleResponse {
            api.uploadRecipePicture(recipeId, MultipartBody.Part.createFormData(KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file))
        }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().link)
    }

    override suspend fun deletePicture(recipeId : String, name: String): SimpleAction =
        handleResponse { api.deleteRecipePicture(recipeId, name) }.toActionStatus().asEmpty()

    companion object {
        private const val KEY_CONTENT_TYPE = "image/jpeg"
        private const val KEY_FORM_DATA_NAME = "file"
        private const val KEY_FIELD_NAME = "file"
    }

}
