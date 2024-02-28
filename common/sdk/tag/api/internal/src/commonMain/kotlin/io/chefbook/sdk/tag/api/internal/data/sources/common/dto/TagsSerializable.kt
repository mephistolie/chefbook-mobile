package io.chefbook.sdk.tag.api.internal.data.sources.common.dto

import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagsSerializable(
  @SerialName("tags")
  val tags: List<TagSerializable> = emptyList(),
  @SerialName("groups")
  val groups: Map<String, String> = emptyMap(),
) {

  fun toEntity() = tags.map { tag -> tag.toEntity(groups) }
}

fun List<Tag>.toSerializable() =
  TagsSerializable(
    tags = map(Tag::toSerializable),
    groups = mapNotNull { it.group }.distinct().associate { it.id to it.name }
  )
