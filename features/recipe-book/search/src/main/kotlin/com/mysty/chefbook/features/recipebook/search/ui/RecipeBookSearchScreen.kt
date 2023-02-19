package com.mysty.chefbook.features.recipebook.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.mysty.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenEffect
import com.mysty.chefbook.features.recipebook.search.ui.navigation.IRecipeBookSearchScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Destination(route = "recipe_book/search")
@Composable
internal fun RecipeBookSearchScreen(
    navigator: IRecipeBookSearchScreenNavigator,
) {
    val viewModel: IRecipeBookSearchScreenViewModel = getViewModel<RecipeBookSearchScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    RecipeBookSearchScreenContent(
        state = state.value,
        onIntent = viewModel::handleIntent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RecipeBookSearchScreenEffect.OnCategoryOpened -> {
                    navigator.openCategoryRecipesScreen(categoryId = effect.categoryId)
                }
                is RecipeBookSearchScreenEffect.OnRecipeOpened -> {
                    navigator.openRecipeScreen(recipeId = effect.recipeId)
                }
                is RecipeBookSearchScreenEffect.Back -> {
                    keyboardController?.hide()
                    navigator.navigateUp()
                }
            }
        }
    }
}
