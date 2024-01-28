package io.chefbook.sdk.category.api.external.domain.entities

import io.chefbook.libs.utils.uuid.generateUUID

data class CategoryInput(
  val name: String = "",
  val emoji: String? = null,
)

fun Category.toInput() =
  CategoryInput(
    name = name.trim(),
    emoji = emoji?.trim(),
  )

fun CategoryInput.toCategory(id: String = generateUUID()) =
  Category(
    id = id,
    name = name.trim(),
    emoji = emoji?.trim(),
  )
