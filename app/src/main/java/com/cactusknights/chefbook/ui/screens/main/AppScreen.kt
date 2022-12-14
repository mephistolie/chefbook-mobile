package com.cactusknights.chefbook.ui.screens.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.main.models.AppEffect
import com.cactusknights.chefbook.ui.screens.main.views.AppScreenDisplay
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mysty.chefbook.core.ui.compose.providers.UiDependenciesProvider
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun AppScreen(
    viewModel: AppViewModel = getViewModel()
) {

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val bottomSheetNavigator = remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    val appState = viewModel.appState.collectAsState()

    UiDependenciesProvider(
        imageClient = viewModel.imageClient,
    ) {
        AppScreenDisplay(
            state = appState.value,
            navController = navController,
            bottomSheetNavigator = bottomSheetNavigator,
            sheetState = sheetState,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.appEffect.collect { effect ->
            if (effect is AppEffect.SignOut) {
                navController.navigate(Destination.Auth.route) {
                    popUpTo(Destination.Auth.route) { inclusive = true }
                }
            }
        }
    }

}
