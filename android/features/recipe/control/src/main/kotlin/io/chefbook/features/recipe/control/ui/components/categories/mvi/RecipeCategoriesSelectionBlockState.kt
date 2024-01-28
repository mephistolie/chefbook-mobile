package io.chefbook.features.recipe.control.ui.components.categories.mvi

import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.libs.mvi.MviState

data class RecipeCategoriesSelectionBlockState(
  val recipe: DecryptedRecipeInfo,
  val categories: List<io.chefbook.sdk.category.api.external.domain.entities.Category> = emptyList(),
  val selectedCategories: List<String> = recipe.categories.map { it.id },
  val isLoading: Boolean = false,
) : MviState
