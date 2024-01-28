package io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.CookingItem
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable.Companion.TYPE_SECTION
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable.Companion.TYPE_STEP
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookingItemSerializable(
  @SerialName("id")
  val id: String,
  @SerialName("text")
  val text: String,
  @SerialName("type")
  val type: String,
  @SerialName("time")
  val time: Int? = null,
  @SerialName("recipeId")
  val recipeId: String? = null,
) {
  fun toEntity(pictures: List<String>?) = when (this.type.lowercase()) {
    TYPE_SECTION -> CookingItem.Section(
      id = id,
      name = text,
    )

    else -> CookingItem.Step(
      id = id,
      description = text,
      time = time,
      pictures = pictures.orEmpty(),
      recipeId = recipeId,
    )
  }

  companion object {
    const val TYPE_STEP = "step"
    const val TYPE_SECTION = "section"
    const val TYPE_ENCRYPTED_DATA = "encrypted_data"
  }
}

fun CookingItem.toSerializable() = when (this) {
  is CookingItem.Step -> CookingItemSerializable(
    id = id,
    text = description,
    time = time,
    type = TYPE_STEP,
    recipeId = recipeId,
  )

  is CookingItem.Section -> CookingItemSerializable(
    id = id,
    text = name,
    type = TYPE_SECTION,
  )
}
