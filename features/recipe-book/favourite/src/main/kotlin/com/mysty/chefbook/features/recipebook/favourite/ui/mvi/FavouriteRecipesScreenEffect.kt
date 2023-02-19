package com.mysty.chefbook.features.recipebook.favourite.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class FavouriteRecipesScreenEffect : MviSideEffect {
    data class OnRecipeOpened(val recipeId: String): FavouriteRecipesScreenEffect()
    object Back: FavouriteRecipesScreenEffect()
}
