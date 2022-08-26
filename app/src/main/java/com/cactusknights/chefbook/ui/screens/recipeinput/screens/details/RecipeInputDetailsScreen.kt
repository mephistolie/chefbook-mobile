package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.RecipeInputDetailsScreenDisplay

@Composable
fun RecipeInputDetailsScreen(
    viewModel: RecipeInputScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    RecipeInputDetailsScreenDisplay(
        state = state.value.input,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

}
