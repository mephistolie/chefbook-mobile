package com.cactusknights.chefbook.ui.screens.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenEffect
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenEvent
import com.cactusknights.chefbook.ui.screens.category.views.CategoryScreenDisplay

@Composable
fun CategoryScreen(
    categoryId: Int,
    appController: NavHostController,
    categoryScreenViewModel: CategoryScreenViewModel = hiltViewModel(),
) {
    val state = categoryScreenViewModel.state.collectAsState()

    CategoryScreenDisplay(
        state = state.value,
        onEvent = { event -> categoryScreenViewModel.obtainEvent(event) },
    )

    LaunchedEffect(Unit) {
        categoryScreenViewModel.obtainEvent(CategoryScreenEvent.LoadRecipesInCategory(categoryId))
        categoryScreenViewModel.effect.collect { effect ->
            when (effect) {
                is CategoryScreenEffect.OnRecipeOpened -> {
                    appController.navigate(Destination.Recipe.route(effect.recipeId))
                }
                is CategoryScreenEffect.Back -> {
                    appController.popBackStack()
                }
            }
        }
    }

}