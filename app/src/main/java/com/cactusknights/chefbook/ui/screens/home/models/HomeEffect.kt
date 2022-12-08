package com.cactusknights.chefbook.ui.screens.home.models

sealed class HomeEffect {
    object RecipeBookOpened : HomeEffect()
    object ShoppingListOpened : HomeEffect()
}
