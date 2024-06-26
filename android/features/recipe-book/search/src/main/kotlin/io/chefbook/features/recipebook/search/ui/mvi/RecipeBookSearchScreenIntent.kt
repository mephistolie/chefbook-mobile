package io.chefbook.features.recipebook.search.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface RecipeBookSearchScreenIntent : MviIntent {

    data class Search(val query: String): RecipeBookSearchScreenIntent

    data class OpenCategoryScreen(val categoryId: String) : RecipeBookSearchScreenIntent

    data class OpenRecipeScreen(val recipeId: String) : RecipeBookSearchScreenIntent

    data object SearchInCommunity : RecipeBookSearchScreenIntent

    data object Back : RecipeBookSearchScreenIntent
}
