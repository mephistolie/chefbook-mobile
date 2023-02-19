package com.cactusknights.chefbook.navigation.graphs

import androidx.compose.runtime.Composable
import com.cactusknights.chefbook.navigation.navigators.AppNavigator
import com.mysty.chefbook.features.home.ui.navigation.IHomeScreenNavGraph
import com.mysty.chefbook.features.recipebook.dashboard.ui.RecipeBookScreen
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.ShoppingListScreen

class HomeScreenNavGraph(
    private val navigator: AppNavigator
) : IHomeScreenNavGraph {

    @Composable
    override fun RecipeBookScreen() = RecipeBookScreen(navigator = navigator)

    @Composable
    override fun ShoppingListScreen() = ShoppingListScreen(navigator = navigator)

}
