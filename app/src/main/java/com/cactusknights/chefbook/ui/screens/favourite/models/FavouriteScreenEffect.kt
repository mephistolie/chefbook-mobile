package com.cactusknights.chefbook.ui.screens.favourite.models

sealed class FavouriteScreenEffect {
    data class OnRecipeOpened(val recipeId: Int): FavouriteScreenEffect()
    object Back: FavouriteScreenEffect()
}
