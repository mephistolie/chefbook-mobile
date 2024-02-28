package io.chefbook.sdk.recipe.book.api.internal.data.models

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

data class RecipeState(
  val id: String,

  val ownerName: String? = null,
  val ownerAvatar: String? = null,

  val version: Int,

  val rating: RecipeMeta.Rating,

  val tags: List<Tag>,
  val categories: List<Category>,
  val isFavourite: Boolean,
)
