package com.cactusknights.chefbook.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.home.models.HomeEffect
import com.cactusknights.chefbook.ui.screens.home.views.HomeScreenDisplay
import com.cactusknights.chefbook.ui.screens.main.models.AppState

@Composable
fun HomeScreen(
    appState: AppState,
    appController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeController = rememberNavController()

    HomeScreenDisplay(
        appState = appState,
        appController = appController,
        dashboardController = homeController,
        onEvent = homeViewModel::obtainEvent,
    )

    LaunchedEffect(Unit) {
        homeViewModel.homeEffect.collect { effect ->
            when (effect) {
                HomeEffect.RecipeBookOpened -> {
                    homeController.navigate(Destination.Home.RecipeBook.route) {
                        popUpTo(Destination.Home.RecipeBook.route) { inclusive = true }
                    }
                }
                HomeEffect.ShoppingListOpened -> {
                    homeController.navigate(Destination.Home.ShoppingList.route) {
                        popUpTo(Destination.Home.ShoppingList.route) { inclusive = true }
                    }
                }
                else -> {}
            }
        }
    }
}
