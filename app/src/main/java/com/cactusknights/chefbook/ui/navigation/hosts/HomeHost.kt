package com.cactusknights.chefbook.ui.navigation.hosts

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.recipebook.RecipeBookScreen

@Composable
fun HomeHost(
    defaultTab: Tab,
    appController: NavHostController,
    dashboardController: NavHostController,
    sheetProgress: Float,
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
                sheetProgress = sheetProgress,
            )
        }
        composable(Destination.Home.ShoppingList.route) {
            Text(text = "Shopping List")
        }
    }
