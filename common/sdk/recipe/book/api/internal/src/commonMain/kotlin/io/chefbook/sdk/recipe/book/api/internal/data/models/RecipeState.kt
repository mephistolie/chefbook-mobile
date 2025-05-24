package io.chefbook.sdk.recipe.book.api.internal.data.models

import io.chefbook.sdk.recipe.core.api.external.domain.entities.CollectionInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

data class RecipeState(
  val id: String,

  val version: Int,

  val rating: RecipeMeta.Rating,

  val tags: List<Tag>,
  val collections: List<CollectionInfo>,
  val isFavourite: Boolean,
)
