package com.cactusknights.chefbook.ui.screens.shoppinglist.models

sealed class ShoppingListEffect {
    data class OnRecipeOpened(
        val recipeId: String
    ) : ShoppingListEffect()
}
