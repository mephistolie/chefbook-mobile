package io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeTagBody(
  @SerialName("name")
  val name: String,
  @SerialName("emoji")
  val emoji: String? = null,
)
