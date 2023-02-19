package com.mysty.chefbook.features.home.ui.navigation

import androidx.compose.runtime.Composable

interface IHomeScreenNavigator {
    fun openAboutScreen()
}

interface IHomeScreenNavGraph {
    @Composable
    fun RecipeBookScreen()
    @Composable
    fun ShoppingListScreen()
}