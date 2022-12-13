package com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views.RecipeInputIngredientScreenDisplay
import org.koin.androidx.compose.getViewModel

@Composable
fun RecipeInputIngredientScreen(
    viewModel: RecipeInputScreenViewModel = getViewModel(),
) {
    val state = viewModel.state.collectAsState()

    RecipeInputIngredientScreenDisplay(
        state = state.value.input,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

}
