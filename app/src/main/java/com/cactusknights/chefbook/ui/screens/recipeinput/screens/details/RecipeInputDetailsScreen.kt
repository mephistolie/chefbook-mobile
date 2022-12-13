package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.RecipeInputDetailsScreenDisplay
import org.koin.androidx.compose.getViewModel

@Composable
fun RecipeInputDetailsScreen(
    viewModel: RecipeInputScreenViewModel = getViewModel(),
) {
    val state = viewModel.state.collectAsState()

    RecipeInputDetailsScreenDisplay(
        state = state.value.input,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

}
