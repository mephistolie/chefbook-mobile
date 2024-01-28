package io.chefbook.sdk.category.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateCategoryRequestBody(
  @SerialName("name")
  val name: String,
  @SerialName("emoji")
  val emoji: String? = null,
)

internal fun CategoryInput.toUpdateCategoryRequest() =
  UpdateCategoryRequestBody(
    name = name,
    emoji = emoji,
  )
