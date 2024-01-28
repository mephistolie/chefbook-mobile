package io.chefbook.sdk.recipe.crud.impl.data.sources.local

import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.PicturesSerializable
import io.chefbook.sdk.recipe.crud.impl.data.models.PictureUploading
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePictures
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePicturesUpdate
import io.chefbook.sdk.recipe.crud.impl.data.sources.RecipePicturesSource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class LocalRecipePicturesSourceImpl(
  database: ChefBookDatabase,
) : DatabaseDataSource(), RecipePicturesSource {

  private val queries = database.recipeQueries

  override suspend fun generatePicturesUploadLinks(
    recipeId: String,
    count: Int
  ): Result<List<PictureUploading>> {
    val uploads = mutableListOf<PictureUploading>()
    repeat(count) {
      uploads.add(
        PictureUploading(picturePath = "$RECIPES_DIR/$recipeId/pictures/${generateUUID()}")
      )
    }
    return Result.success(uploads)
  }

  override suspend fun setPictures(recipeId: String, pictures: RecipePictures, version: Int?): Result<RecipePicturesUpdate> {
    val serializable = PicturesSerializable(preview = pictures.preview, cooking = pictures.cooking)
    val encodedPictures = Json.encodeToString(serializable)
    return safeQueryResult {
      queries.setPictures(encodedPictures, recipeId)
      RecipePicturesUpdate(
        pictures = pictures,
        version = queries.getVersion(recipeId).executeAsOne().toInt(),
      )
    }
  }

  companion object {
    const val RECIPES_DIR = "recipes"
  }
}
