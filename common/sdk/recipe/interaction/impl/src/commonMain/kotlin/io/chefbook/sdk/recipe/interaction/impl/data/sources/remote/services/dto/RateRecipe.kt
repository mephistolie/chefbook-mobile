package io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RateRecipeRequest(
  @SerialName("score")
  val score: Int?,
)
