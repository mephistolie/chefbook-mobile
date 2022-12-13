package com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.views.RecipeInputCookingScreenDisplay
import org.koin.androidx.compose.getViewModel

@Composable
fun RecipeInputCookingScreen(
    viewModel: RecipeInputScreenViewModel = getViewModel(),
) {
    val state = viewModel.state.collectAsState()

    RecipeInputCookingScreenDisplay(
        state = state.value.input,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

}
