package io.chefbook.sdk.recipe.book.api.internal.data.models

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta

data class RecipeState(
  val id: String,

  val ownerName: String? = null,
  val ownerAvatar: String? = null,

  val version: Int,

  val rating: RecipeMeta.Rating,

  val categories: List<Category> = emptyList(),
  val isFavourite: Boolean = false,
)
