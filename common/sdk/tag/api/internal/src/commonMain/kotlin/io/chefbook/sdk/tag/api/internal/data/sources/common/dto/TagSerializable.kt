package io.chefbook.sdk.tag.api.internal.data.sources.common.dto

import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagSerializable(
  @SerialName("tagId")
  val id: String,
  @SerialName("name")
  val name: String,
  @SerialName("emoji")
  val emoji: String? = null,
  @SerialName("groupId")
  val groupId: String? = null,
) {

  fun toEntity(groups: Map<String, String>) =
    Tag(
      id = id,
      name = name,
      emoji = emoji,
      group = groupId?.let {
        val groupName = groups[groupId] ?: return@let null
        TagGroup(
          id = groupId,
          name = groupName,
        )
      }
    )
}

fun Tag.toSerializable() =
  TagSerializable(
    id = id,
    name = name,
    emoji = emoji,
    groupId = group?.id,
  )
