package com.cactusknights.chefbook.ui.screens.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.ui.screens.about.models.AboutScreenEffect
import com.cactusknights.chefbook.ui.screens.about.views.AboutScreenDisplay
import org.koin.androidx.compose.getViewModel

@Composable
fun AboutScreen(
    appController: NavHostController,
    viewModel: AboutScreenViewModel = getViewModel(),
) {

    AboutScreenDisplay(
        onEvent = { event -> viewModel.obtainEvent(event) },
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AboutScreenEffect.OnBack -> {
                    appController.popBackStack()
                }
            }
        }
    }
}
