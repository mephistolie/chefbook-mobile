package com.mysty.chefbook.features.recipebook.category.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenEffect
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import com.mysty.chefbook.features.recipebook.category.ui.navigation.ICategoryRecipesScreenNavigator
import com.mysty.chefbook.navigation.results.category.CategoryActionResult
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "recipe_book/category")
@Composable
fun CategoryRecipesScreen(
    categoryId: String,
    navigator: ICategoryRecipesScreenNavigator,
    categoryInputRecipient: OpenResultRecipient<CategoryActionResult>
) {
    val viewModel: ICategoryRecipesScreenViewModel = getViewModel<CategoryRecipesScreenViewModel> { parametersOf(categoryId) }
    val state = viewModel.state.collectAsState()

    CategoryScreenContent(
        state = state.value,
        onIntent = viewModel::handleIntent,
    )

    categoryInputRecipient.onNavResult { navResult ->
        if (navResult is NavResult.Value) {
            when (val result = navResult.value) {
                is CategoryActionResult.Updated -> {
                    viewModel.handleIntent(CategoryScreenIntent.OnCategoryUpdated(
                        Category(
                            id = result.id,
                            name = result.name,
                            cover = result.cover,
                        )
                    ))
                }
                is CategoryActionResult.Deleted -> viewModel.handleIntent(CategoryScreenIntent.Back)
                else -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoryScreenEffect.OpenRecipeScreen -> navigator.openRecipeScreen(recipeId = effect.recipeId)
                is CategoryScreenEffect.OpenCategoryInputDialog -> navigator.openCategoryInputDialog(categoryId = effect.categoryId)
                is CategoryScreenEffect.Back -> navigator.navigateUp()
            }
        }
    }

}
