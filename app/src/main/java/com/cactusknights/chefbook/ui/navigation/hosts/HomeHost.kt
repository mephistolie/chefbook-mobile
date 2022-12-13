package com.cactusknights.chefbook.ui.navigation.hosts

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenTab
import com.cactusknights.chefbook.ui.screens.recipebook.RecipeBookScreen
import com.cactusknights.chefbook.ui.screens.shoppinglist.ShoppingListScreen

private const val EXPANDED_PROGRESS_VALUE = 1F

@Composable
fun HomeHost(
    defaultTab: Tab,
    appController: NavHostController,
    dashboardController: NavHostController,
    expandProgress: Float,
) =
    NavHost(
        navController = dashboardController,
        startDestination =
        if (defaultTab == Tab.RECIPE_BOOK) {
            Destination.Home.RecipeBook.route
        } else {
            Destination.Home.ShoppingList.route
        },
        route = Destination.Home.route,
    ) {
        composable(Destination.Home.RecipeBook.route) {
            RecipeBookScreen(
                navController = appController,
                topBarScale = EXPANDED_PROGRESS_VALUE - expandProgress,
            )
        }
        composable(Destination.Home.ShoppingList.route) {
            ShoppingListScreen(
                onOpenRecipe = { recipeId ->
                    appController.navigate(Destination.Recipe.route(recipeId, defaultTab = RecipeScreenTab.Ingredients))
                }
            )
        }
    }
