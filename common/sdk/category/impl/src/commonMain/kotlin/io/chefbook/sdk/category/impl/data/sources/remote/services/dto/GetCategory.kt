package io.chefbook.sdk.category.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryBody(
  @SerialName("categoryId")
  val id: String,
  @SerialName("name")
  val name: String,
  @SerialName("cover")
  val cover: String? = null,
)

internal fun CategoryBody.toEntity() =
  Category(
    id = id,
    name = name,
    emoji = cover,
  )
