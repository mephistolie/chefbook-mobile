package io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRecipeKeyResponse(
  @SerialName("key")
  val key: String? = null,
)
