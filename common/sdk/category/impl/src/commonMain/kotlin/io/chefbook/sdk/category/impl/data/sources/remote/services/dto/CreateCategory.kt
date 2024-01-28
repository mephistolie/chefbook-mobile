package io.chefbook.sdk.category.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateCategoryRequestBody(
  @SerialName("categoryId")
  val categoryId: String? = null,
  @SerialName("name")
  val name: String,
  @SerialName("emoji")
  val emoji: String? = null,
)

@Serializable
internal data class CreateCategoryResponseBody(
  @SerialName("categoryId")
  val categoryId: String,
)

internal fun CategoryInput.toCreateCategoryRequest(categoryId: String? = null) =
  CreateCategoryRequestBody(
    categoryId = categoryId,
    name = name,
    emoji = emoji,
  )
