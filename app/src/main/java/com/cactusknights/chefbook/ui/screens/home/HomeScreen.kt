package com.cactusknights.chefbook.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.home.models.HomeEffect
import com.cactusknights.chefbook.ui.screens.home.views.HomeScreenDisplay
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    appState: AppState,
    appController: NavHostController,
    homeViewModel: HomeViewModel = getViewModel()
) {
    val homeController = rememberNavController()
    val homeState = homeViewModel.homeState.collectAsState()

    HomeScreenDisplay(
        appState = appState,
        appController = appController,
        homeState = homeState.value,
        homeController = homeController,
        onEvent = homeViewModel::obtainEvent,
    )

    LaunchedEffect(Unit) {
        homeViewModel.homeEffect.collect { effect ->
            when (effect) {
                is HomeEffect.RecipeBookOpened -> {
                    if (appController.currentDestination?.route != Destination.Home.RecipeBook.route) {
                        homeController.navigate(Destination.Home.RecipeBook.route) {
                            popUpTo(Destination.Home.RecipeBook.route) { inclusive = true }
                        }
                    }
                }
                is HomeEffect.ShoppingListOpened  -> {
                    if (appController.currentDestination?.route != Destination.Home.ShoppingList.route) {
                        homeController.navigate(Destination.Home.ShoppingList.route) {
                            popUpTo(Destination.Home.ShoppingList.route) { inclusive = true }
                        }
                    }
                }
                is HomeEffect.ProfileOpened -> {
                    appController.navigate(Destination.About.route) {
                        popUpTo(Destination.About.route) { inclusive = true }
                    }
                }
            }
        }
    }
}
