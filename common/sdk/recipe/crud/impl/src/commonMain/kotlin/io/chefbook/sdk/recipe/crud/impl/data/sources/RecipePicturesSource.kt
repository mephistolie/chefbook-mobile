package io.chefbook.sdk.recipe.crud.impl.data.sources

import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePictures
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePicturesUpdate

internal interface RecipePicturesSource {
  suspend fun generatePicturesUploadLinks(
    recipeId: String,
    count: Int
  ): Result<List<PictureUploading>>

  suspend fun setPictures(
    recipeId: String,
    pictures: RecipePictures,
    version: Int? = null
  ): Result<RecipePicturesUpdate>
}
