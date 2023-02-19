package com.mysty.chefbook.features.recipebook.search.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class RecipeBookSearchScreenEffect : MviSideEffect {
    data class OnCategoryOpened(val categoryId: String): RecipeBookSearchScreenEffect()
    data class OnRecipeOpened(val recipeId: String): RecipeBookSearchScreenEffect()
    object Back: RecipeBookSearchScreenEffect()
}
