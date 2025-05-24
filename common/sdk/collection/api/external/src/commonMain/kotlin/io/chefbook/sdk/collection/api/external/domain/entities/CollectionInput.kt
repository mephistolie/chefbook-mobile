package io.chefbook.sdk.collection.api.external.domain.entities

import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.libs.utils.uuid.generateUUID

data class CollectionInput(
  val id: String,
  val name: String,
  val visibility: Visibility,
) {

  companion object {
    fun new() = CollectionInput(
      id = generateUUID(),
      name = "",
      visibility = Visibility.PRIVATE,
    )
  }
}

fun CollectionInput.toCollection(
  id: String,
  contributors: List<Contributor>,
  recipesCount: Int = 0,
) =
  Collection(
    id = id,
    name = name.trim(),
    visibility = visibility,
    contributors = contributors,
    recipesCount = recipesCount,
  )

fun Collection.toInput(): CollectionInput =
  CollectionInput(
    id = id,
    name = name.trim(),
    visibility = visibility,
  )
