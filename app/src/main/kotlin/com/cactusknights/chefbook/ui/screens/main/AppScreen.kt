package com.cactusknights.chefbook.ui.screens.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.plusAssign
import com.cactusknights.chefbook.navigation.navigators.AppNavigator
import com.cactusknights.chefbook.ui.screens.main.mvi.AppEffect
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mysty.chefbook.core.android.compose.providers.UiDependenciesProvider
import com.mysty.chefbook.features.auth.ui.destinations.AuthScreenDestination
import com.mysty.chefbook.navigation.styles.DismissibleDialog
import com.mysty.chefbook.navigation.styles.NonDismissibleDialog
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.ramcosta.composedestinations.utils.destination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(
  ExperimentalMaterialNavigationApi::class,
  ExperimentalMaterialApi::class,
  ExperimentalAnimationApi::class
)
@Composable
fun AppScreen() {
  val scope = rememberCoroutineScope()

  val appViewModel = getViewModel<AppViewModel>()
  val appState = appViewModel.state.collectAsState()

  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true,
  )
  val bottomSheetNavigator = remember(sheetState) { BottomSheetNavigator(sheetState = sheetState) }

  val navController = rememberAnimatedNavController()
  navController.navigatorProvider += bottomSheetNavigator
  val navigator = AppNavigator(
    navController = navController,
    bottomSheet = bottomSheetNavigator,
    hideBottomSheet = {
      return@AppNavigator if (
        navController.currentBackStackEntry?.destination()?.style is DestinationStyle.BottomSheet
        && sheetState.currentValue == ModalBottomSheetValue.Expanded
        && !sheetState.isAnimationRunning) {
        scope.launch { sheetState.hide() }
        true
      } else {
        false
      }
    }
  )

  val currentDestination = navController.currentBackStackEntryFlow.collectAsState(null)
  val isBackgroundBlurred = currentDestination.value?.destination()?.style is DestinationStyle.Dialog

  UiDependenciesProvider(imageClient = appViewModel.imageClient) {
    AppScreenContent(
      state = appState.value,
      isBackgroundBlurred = isBackgroundBlurred,
      navigator = navigator,
    )
  }

  LaunchedEffect(Unit) {
    launch {

    }
    launch {
      appViewModel.effect.collect { effect ->
        when (effect) {
          is AppEffect.SignedOut -> navController.navigate(AuthScreenDestination)
        }
      }
    }
  }

}

private val dialogDestinationRoutes = listOf(
  DismissibleDialog,
  NonDismissibleDialog
)