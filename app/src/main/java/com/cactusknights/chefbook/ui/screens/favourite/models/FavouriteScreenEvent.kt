package com.cactusknights.chefbook.ui.screens.favourite.models

sealed class FavouriteScreenEvent {
    data class OpenRecipeScreen(val recipeId: String) : FavouriteScreenEvent()
    object Back : FavouriteScreenEvent()
}
