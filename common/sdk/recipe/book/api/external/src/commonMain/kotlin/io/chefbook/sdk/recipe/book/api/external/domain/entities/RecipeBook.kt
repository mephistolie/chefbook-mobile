package io.chefbook.sdk.recipe.book.api.external.domain.entities

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo

data class RecipeBook(
  val recipes: List<RecipeInfo>,
  val categories: List<Category>,
)
