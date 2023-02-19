package com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.mvi.MviState

data class RecipeCategoriesSelectionBlockState(
  val recipe: RecipeInfo,
  val categories: List<Category> = emptyList(),
  val selectedCategories: List<String> = recipe.categories.map { it.id },
  val isLoading: Boolean = false,
) : MviState
