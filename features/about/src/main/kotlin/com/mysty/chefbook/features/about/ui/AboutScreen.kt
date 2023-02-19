package com.mysty.chefbook.features.about.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mysty.chefbook.features.about.ui.mvi.AboutScreenEffect
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

@Destination(route = "about")
@Composable
internal fun AboutScreen(
    versionName: String,
    navigator: IBaseNavigator,
) {
    val viewModel: IAboutScreenViewModel = getViewModel<AboutScreenViewModel>()

    AboutScreenContent(
        versionName = versionName,
        onIntent = { event -> viewModel.handleIntent(event) },
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AboutScreenEffect.OnBack -> {
                    navigator.navigateUp()
                }
            }
        }
    }
}
