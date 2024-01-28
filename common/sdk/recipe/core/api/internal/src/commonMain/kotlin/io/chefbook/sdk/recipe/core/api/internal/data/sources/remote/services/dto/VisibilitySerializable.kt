package io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VisibilitySerializable {

  @SerialName("private")
  PRIVATE,

  @SerialName("link")
  LINK,

  @SerialName("public")
  PUBLIC,
}
