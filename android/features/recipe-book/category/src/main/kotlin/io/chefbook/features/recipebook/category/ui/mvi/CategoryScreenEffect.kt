package io.chefbook.features.recipebook.category.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class CategoryScreenEffect : MviSideEffect {
  data class OpenRecipeScreen(val recipeId: String) : CategoryScreenEffect()
  data class OpenCategoryInputDialog(val categoryId: String) : CategoryScreenEffect()
  data object Back : CategoryScreenEffect()
}
