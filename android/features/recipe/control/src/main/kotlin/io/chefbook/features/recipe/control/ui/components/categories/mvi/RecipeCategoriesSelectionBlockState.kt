package io.chefbook.features.recipe.control.ui.components.categories.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

data class RecipeCategoriesSelectionBlockState(
  val recipe: DecryptedRecipeInfo,
  val categories: List<Collection> = emptyList(),
  val selectedCategories: List<String> = recipe.collections.map { it.id },
  val isLoading: Boolean = false,
) : MviState
