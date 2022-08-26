package com.cactusknights.chefbook.ui.screens.home.models

sealed class HomeEffect {
    object RecipeBookOpened : HomeEffect()
    object OnlineRecipesOpened : HomeEffect()
    object ShoppingListOpened : HomeEffect()
    object ProfileOpened : HomeEffect()
}
