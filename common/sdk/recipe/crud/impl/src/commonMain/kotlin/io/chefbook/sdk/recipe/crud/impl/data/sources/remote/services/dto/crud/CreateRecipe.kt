package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class CreateRecipeResponse(
  @SerialName("recipeId")
  val recipeId: String,
  @SerialName("version")
  val version: Int,
)
