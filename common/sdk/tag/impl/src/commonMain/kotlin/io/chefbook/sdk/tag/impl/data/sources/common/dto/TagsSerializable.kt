package io.chefbook.sdk.tag.impl.data.sources.common.dto

import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TagsSerializable(
  @SerialName("tags")
  val tags: List<TagSerializable> = emptyList(),
  @SerialName("groups")
  val groups: Map<String, String> = emptyMap(),
) {

  fun toEntity() = tags.map { tag ->
    Tag(
      id = tag.id,
      name = tag.name,
      emoji = tag.emoji,
      group = tag.groupId?.let { groupId ->
        val groupName = groups[groupId] ?: return@let null
        TagGroup(
          id = groupId,
          name = groupName,
        )
      }
    )
  }
}

internal fun List<Tag>.toSerializable() =
  TagsSerializable(
    tags = map { tag ->
       TagSerializable(
         id = tag.id,
         name = tag.name,
         emoji = tag.emoji,
         groupId = tag.group?.id,
       )
    },
    groups = mapNotNull { it.group }.distinct().associate { it.id to it.name }
  )
