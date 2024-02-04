package io.chefbook.sdk.recipe.crud.impl.data.sources.remote

import io.chefbook.libs.utils.result.withCast
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePictures
import io.chefbook.sdk.recipe.crud.impl.data.sources.RecipePicturesSource
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipePicturesApiService
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.GenerateRecipePicturesUploadLinksRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.PictureUploadResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.SetRecipePicturesRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.SetRecipePicturesResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.toModel
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures.toSerializable

internal class RemoteRecipePicturesSourceImpl(
  private val api: RecipePicturesApiService,
) : RecipePicturesSource {

  override suspend fun generatePicturesUploadLinks(recipeId: String, count: Int) =
    api.generateRecipePicturesUploadLinks(recipeId, GenerateRecipePicturesUploadLinksRequest(count))
      .withCast { uploads -> uploads.map(PictureUploadResponse::toModel) }

  override suspend fun setPictures(
    recipeId: String,
    pictures: RecipePictures,
    version: Int?,
  ) =
    api.setRecipePicture(
      recipeId = recipeId,
      body = SetRecipePicturesRequest(pictures = pictures.toSerializable(), version = version)
    )
      .withCast(SetRecipePicturesResponse::toEntity)
}

