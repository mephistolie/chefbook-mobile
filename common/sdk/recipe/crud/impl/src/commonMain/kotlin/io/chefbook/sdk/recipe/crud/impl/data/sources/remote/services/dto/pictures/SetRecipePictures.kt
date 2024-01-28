package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures

import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePictures
import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePicturesUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetRecipePicturesRequest(
  @SerialName("pictures")
  val pictures: PicturesBody,
  @SerialName("version")
  val version: Int? = null,
)

@Serializable
internal data class SetRecipePicturesResponse(
  @SerialName("pictures")
  val pictures: PicturesBody,
  @SerialName("version")
  val version: Int,
) {

  fun toEntity() =
    RecipePicturesUpdate(
      pictures = pictures.toEntity(),
      version = version,
    )
}
