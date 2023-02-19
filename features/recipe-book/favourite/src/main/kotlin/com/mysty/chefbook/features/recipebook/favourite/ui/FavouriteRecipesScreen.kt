package com.mysty.chefbook.features.recipebook.favourite.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenEffect
import com.mysty.chefbook.features.recipebook.favourite.ui.navigation.IRecipeBookFavouriteScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

@Destination(route = "recipe_book/favourite")
@Composable
internal fun FavouriteRecipesScreen(
    navigator: IRecipeBookFavouriteScreenNavigator,
) {
    val viewModel: IFavouriteRecipesScreenViewModel = getViewModel<FavouriteRecipesScreenViewModel>()
    val state = viewModel.state.collectAsState()

    FavouriteRecipesScreenContent(
        state = state.value,
        onIntent = viewModel::handleIntent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FavouriteRecipesScreenEffect.OnRecipeOpened -> navigator.openRecipeScreen(recipeId = effect.recipeId)
                is FavouriteRecipesScreenEffect.Back -> navigator.navigateUp()
            }
        }
    }
}
