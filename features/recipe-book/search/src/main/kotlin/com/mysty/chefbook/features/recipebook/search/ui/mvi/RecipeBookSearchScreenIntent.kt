package com.mysty.chefbook.features.recipebook.search.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class RecipeBookSearchScreenIntent : MviIntent {
    data class Search(val query: String): RecipeBookSearchScreenIntent()
    data class OpenCategoryScreen(val categoryId: String) : RecipeBookSearchScreenIntent()
    data class OpenRecipeScreen(val recipeId: String) : RecipeBookSearchScreenIntent()
    object Back : RecipeBookSearchScreenIntent()
}
