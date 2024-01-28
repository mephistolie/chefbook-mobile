package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures

import io.chefbook.sdk.recipe.crud.impl.data.models.RecipePictures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PicturesBody(
  @SerialName("preview")
  val preview: String? = null,
  @SerialName("cooking")
  val cooking: Map<String, List<String>>? = null,
) {

  fun toEntity() =
    RecipePictures(
      preview = preview,
      cooking = cooking ?: emptyMap(),
    )
}

internal fun RecipePictures.toSerializable() =
  PicturesBody(
    preview = preview,
    cooking = cooking.ifEmpty { null },
  )
