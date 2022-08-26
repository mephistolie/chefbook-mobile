package com.cactusknights.chefbook.ui.screens.favourite.models

sealed class FavouriteScreenEvent {
    data class OpenRecipeScreen(val recipeId: Int) : FavouriteScreenEvent()
    object Back : FavouriteScreenEvent()
}
