package io.chefbook.sdk.tag.impl.data.sources.common.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TagSerializable(
  @SerialName("tagId")
  val id: String,
  @SerialName("name")
  val name: String,
  @SerialName("emoji")
  val emoji: String? = null,
  @SerialName("groupId")
  val groupId: String? = null,
)
