package com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views.RecipeInputIngredientScreenDisplay

@Composable
fun RecipeInputIngredientScreen(
    viewModel: RecipeInputScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    RecipeInputIngredientScreenDisplay(
        state = state.value.input,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

}
