package io.chefbook.ui.screens.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.utils.destination
import io.chefbook.core.android.compose.providers.ui.UiDependenciesProvider
import io.chefbook.features.auth.ui.destinations.AuthScreenDestination
import io.chefbook.navigation.navigators.AppNavigator
import io.chefbook.ui.screens.main.mvi.AppEffect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(
  ExperimentalMaterialNavigationApi::class,
  ExperimentalMaterialApi::class,
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

  val navController = rememberNavController()
  navController.navigatorProvider += bottomSheetNavigator
  val navigator = AppNavigator(
    navController = navController,
    bottomSheet = bottomSheetNavigator,
    hideBottomSheet = {
      return@AppNavigator if (
        navController.currentBackStackEntry?.destination()?.style is DestinationStyleBottomSheet
        && sheetState.currentValue == ModalBottomSheetValue.Expanded
      ) {
        scope.launch { sheetState.hide() }
        true
      } else {
        false
      }
    }
  )

  val currentDestination = navController.currentBackStackEntryFlow.collectAsState(null)
  val destinationStyle = currentDestination.value?.destination()?.style
  val isBackgroundBlurred = destinationStyle is DestinationStyle.Dialog

  UiDependenciesProvider(imageClient = appViewModel.imageClient) {
    AppScreenContent(
      state = appState.value,
      isBackgroundBlurred = isBackgroundBlurred,
      navigator = navigator,
    )
  }

  LaunchedEffect(Unit) {
    appViewModel.effect.collect { effect ->
      if (appState.value.isSignedIn == null) return@collect

      when (effect) {
        is AppEffect.SignedOut -> {
          val destination = navController.currentBackStackEntry?.destination()
          if (destination != null && destination != AuthScreenDestination) navigator.openAuthScreen()
        }
      }
    }
  }
}
