package io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PicturesSerializable(
  @SerialName("preview")
  val preview: String?,
  @SerialName("cooking")
  val cooking: Map<String, List<String>>,
)
