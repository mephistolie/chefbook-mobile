package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.GenerateRecipePicturesUploadLinksRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.PictureUploadResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.SetRecipePicturesRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.SetRecipePicturesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class RecipePicturesApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), RecipePicturesApiService {

  override suspend fun generateRecipePicturesUploadLinks(
    recipeId: String,
    body: GenerateRecipePicturesUploadLinksRequest
  ): Result<List<PictureUploadResponse>> = safePost {
    url("$RECIPES_ROUTE/$recipeId/pictures")
    setBody(body)
  }

  override suspend fun setRecipePicture(
    recipeId: String,
    body: SetRecipePicturesRequest
  ): Result<SetRecipePicturesResponse> = safePut {
    url("$RECIPES_ROUTE/$recipeId/pictures")
    setBody(body)
  }

  companion object {
    private const val RECIPES_ROUTE = "/v1/recipes"
  }
}