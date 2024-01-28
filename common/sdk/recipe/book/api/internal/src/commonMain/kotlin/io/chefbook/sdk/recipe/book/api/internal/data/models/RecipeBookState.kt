package io.chefbook.sdk.recipe.book.api.internal.data.models

import io.chefbook.sdk.category.api.external.domain.entities.Category

data class RecipeBookState(
  val recipes: List<RecipeState>,
  val categories: List<Category>,
  val isEncryptedVaultEnabled: Boolean,
)
