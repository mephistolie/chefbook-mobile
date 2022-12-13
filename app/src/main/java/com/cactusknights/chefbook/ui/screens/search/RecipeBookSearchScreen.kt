package com.cactusknights.chefbook.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.search.models.RecipeBookSearchScreenEffect
import com.cactusknights.chefbook.ui.screens.search.views.RecipeBookSearchScreenDisplay
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeBookSearchScreen(
    navController: NavHostController,
    viewModel: RecipeBookSearchScreenViewModel = getViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val state = viewModel.state.collectAsState()

    RecipeBookSearchScreenDisplay(
        state = state.value,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RecipeBookSearchScreenEffect.OnCategoryOpened -> {
                    keyboardController?.hide()
                    navController.navigate(Destination.Home.RecipeBook.Category.route(effect.categoryId))
                }
                is RecipeBookSearchScreenEffect.OnRecipeOpened -> {
                    keyboardController?.hide()
                    navController.navigate(Destination.Recipe.route(effect.recipeId))
                }
                is RecipeBookSearchScreenEffect.Back -> {
                    keyboardController?.hide()
                    navController.popBackStack()
                }
            }
        }
    }
}
