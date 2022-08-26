package com.cactusknights.chefbook.data.datasources.remote.recipes

import com.cactusknights.chefbook.data.IRecipePictureSource
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.RecipeApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class RemoteRecipePictureSource @Inject constructor(
    private val api: RecipeApi,
    private val handleResponse: INetworkHandler,
) : IRecipePictureSource {

    override suspend fun getPictures(recipeId: Int): ActionStatus<List<String>> =
        handleResponse { api.getRecipePictures(recipeId) }.toActionStatus()

    override suspend fun addPicture(recipeId: Int, data: ByteArray): ActionStatus<String> {
        val file =  data.toRequestBody(KEY_CONTENT_TYPE.toMediaTypeOrNull())
        val result =  handleResponse {
            api.uploadRecipePicture(recipeId, MultipartBody.Part.createFormData(KEY_FORM_DATA_NAME, KEY_FIELD_NAME, file))
        }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().link)
    }

    override suspend fun deletePicture(recipeId : Int, name: String): SimpleAction =
        handleResponse { api.deleteRecipePicture(recipeId, name) }.toActionStatus().asEmpty()

    companion object {
        private const val KEY_CONTENT_TYPE = "image/jpeg"
        private const val KEY_FORM_DATA_NAME = "file"
        private const val KEY_FIELD_NAME = "file"
    }

}
