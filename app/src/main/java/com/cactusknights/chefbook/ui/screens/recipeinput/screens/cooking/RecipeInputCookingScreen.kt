package com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.views.RecipeInputCookingScreenDisplay

@Composable
fun RecipeInputCookingScreen(
    viewModel: RecipeInputScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    RecipeInputCookingScreenDisplay(
        state = state.value.input,
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

}
