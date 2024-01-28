package io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingBody(
  @SerialName("index")
  val index: Float = 0F,
  @SerialName("score")
  val score: Int? = null,
  @SerialName("votes")
  val votes: Int = 0,
)