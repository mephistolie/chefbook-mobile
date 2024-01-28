package io.chefbook.features.recipebook.search.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class RecipeBookSearchScreenEffect : MviSideEffect {
  data class OnCategoryOpened(val categoryId: String) : RecipeBookSearchScreenEffect()
  data class OnRecipeOpened(val recipeId: String) : RecipeBookSearchScreenEffect()
  data object Back : RecipeBookSearchScreenEffect()
}
