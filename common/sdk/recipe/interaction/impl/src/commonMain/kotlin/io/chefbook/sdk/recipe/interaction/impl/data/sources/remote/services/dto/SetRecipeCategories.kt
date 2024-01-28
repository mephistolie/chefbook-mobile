package io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetRecipeCategoriesRequest(
  @SerialName("categories")
  val categories: List<String>,
)
