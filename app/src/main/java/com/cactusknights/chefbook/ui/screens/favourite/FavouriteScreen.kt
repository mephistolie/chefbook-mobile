package com.cactusknights.chefbook.ui.screens.favourite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.favourite.models.FavouriteScreenEffect
import com.cactusknights.chefbook.ui.screens.favourite.views.FavouriteScreenDisplay
import org.koin.androidx.compose.getViewModel

@Composable
fun FavouriteScreen(
    appController: NavHostController,
    viewModel: FavouriteScreenViewModel = getViewModel(),
) {
    val state = viewModel.state.collectAsState()

    FavouriteScreenDisplay(
        state = state.value,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FavouriteScreenEffect.OnRecipeOpened -> {
                    appController.navigate(Destination.Recipe.route(effect.recipeId))
                }
                is FavouriteScreenEffect.Back -> {
                    if (appController.previousBackStackEntry != null) {
                        appController.popBackStack()
                    }
                }
            }
        }
    }
}
