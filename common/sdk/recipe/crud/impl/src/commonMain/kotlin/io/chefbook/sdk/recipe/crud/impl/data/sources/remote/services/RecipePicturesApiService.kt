package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services

import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.GenerateRecipePicturesUploadLinksRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.PictureUploadResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.SetRecipePicturesRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.SetRecipePicturesResponse

internal interface RecipePicturesApiService {

  suspend fun generateRecipePicturesUploadLinks(
    recipeId: String,
    body: GenerateRecipePicturesUploadLinksRequest,
  ): Result<List<PictureUploadResponse>>

  suspend fun setRecipePicture(
    recipeId: String,
    body: SetRecipePicturesRequest,
  ): Result<SetRecipePicturesResponse>
}
