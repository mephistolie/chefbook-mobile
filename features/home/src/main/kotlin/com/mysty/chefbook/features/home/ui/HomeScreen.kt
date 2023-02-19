package com.mysty.chefbook.features.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenEffect
import com.mysty.chefbook.features.home.ui.navigation.IHomeScreenNavGraph
import com.mysty.chefbook.features.home.ui.navigation.IHomeScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

@Destination(route = "home")
@Composable
fun HomeScreen(
    navGraph: IHomeScreenNavGraph,
    navigator: IHomeScreenNavigator,
) {
    val homeViewModel: IHomeViewModel = getViewModel<HomeViewModel>()
    val homeState = homeViewModel.state.collectAsState()

    HomeScreenContent(
        homeState = homeState.value,
        onIntent = homeViewModel::handleIntent,
        navGraph = navGraph,
    )

    LaunchedEffect(Unit) {
        homeViewModel.effect.collect { effect ->
            when (effect) {
                is HomeScreenEffect.ProfileOpened -> navigator.openAboutScreen()
            }
        }
    }
}
