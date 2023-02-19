package com.mysty.chefbook.features.recipebook.favourite.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class FavouriteRecipesScreenIntent : MviIntent {
    data class OpenRecipeScreen(val recipeId: String) : FavouriteRecipesScreenIntent()
    object Back : FavouriteRecipesScreenIntent()
}
