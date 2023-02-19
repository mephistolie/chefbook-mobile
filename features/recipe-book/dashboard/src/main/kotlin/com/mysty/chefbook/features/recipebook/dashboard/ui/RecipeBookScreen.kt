package com.mysty.chefbook.features.recipebook.dashboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.recipebook.dashboard.ui.mvi.RecipeBookScreenEffect
import com.mysty.chefbook.features.recipebook.dashboard.ui.navigation.IRecipeBookScreenNavigator
import org.koin.androidx.compose.getViewModel

@Composable
fun RecipeBookScreen(
    navigator: IRecipeBookScreenNavigator,
) {
    val viewModel: IRecipeBookScreenViewModel = getViewModel<RecipeBookScreenViewModel>()
    val state = viewModel.state.collectAsState()

    RecipeBookScreenContent(
        state = state.value,
        onIntent = viewModel::handleIntent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RecipeBookScreenEffect.OpenRecipeSearchScreen -> navigator.openRecipeBookSearchScreen()
                is RecipeBookScreenEffect.OpenFavouriteRecipesScreen -> navigator.openFavouriteRecipesScreen()
                is RecipeBookScreenEffect.OpenCategoryRecipesScreen -> navigator.openCategoryRecipesScreen(categoryId = effect.categoryId)
                is RecipeBookScreenEffect.OpenCommunityRecipesScreen -> {}
                is RecipeBookScreenEffect.OpenEncryptedVaultScreen -> navigator.openEncryptedVaultScreen()
                is RecipeBookScreenEffect.OpenRecipeCreationScreen -> navigator.openRecipeInputScreen()
                is RecipeBookScreenEffect.OpenRecipeScreen -> navigator.openRecipeScreen(recipeId = effect.recipeId)
                is RecipeBookScreenEffect.OpenCategoryCreationScreen -> navigator.openCategoryInputDialog()
            }
        }
    }
}
