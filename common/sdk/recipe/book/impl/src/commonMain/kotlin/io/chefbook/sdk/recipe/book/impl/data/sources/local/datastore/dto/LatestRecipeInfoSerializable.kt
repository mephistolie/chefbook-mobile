package io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.dto

import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestRecipeInfoSerializable(
  @SerialName("id")
  val id: String,
  @SerialName("name")
  val name: String,
  @SerialName("preview")
  val preview: String?,
  @SerialName("time")
  val time: Int?,
) {

  fun toEntity() =
    LatestRecipeInfo(
      id = id,
      name = name,
      preview = preview,
      time = time,
    )
}

fun LatestRecipeInfo.toSerializable() =
  LatestRecipeInfoSerializable(
    id = id,
    name = name,
    preview = preview,
    time = time,
  )
