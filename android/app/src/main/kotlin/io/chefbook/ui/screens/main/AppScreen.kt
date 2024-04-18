package io.chefbook.ui.screens.main

import androidx.compose.animation.core.SpringSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import coil.Coil
import coil.ImageLoader
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.utils.destination
import io.chefbook.features.auth.ui.destinations.AuthScreenDestination
import io.chefbook.libs.logger.Logger
import io.chefbook.navigation.navigators.AppNavigator
import io.chefbook.sdk.network.impl.di.qualifiers.HttpClient
import io.chefbook.ui.screens.main.mvi.AppEffect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@OptIn(
  ExperimentalMaterialNavigationApi::class,
  ExperimentalMaterialApi::class,
)
@Composable
fun AppScreen() {
  val scope = rememberCoroutineScope()

  val appViewModel = koinViewModel<AppViewModel>()
  val appState = appViewModel.state.collectAsStateWithLifecycle()

  val okHttpClient = koinInject<OkHttpClient>(named(HttpClient.ENCRYPTED_IMAGE))
  val context = LocalContext.current
  val imageLoader = remember {
    ImageLoader.Builder(context)
      .okHttpClient(okHttpClient)
      .build()
  }

  val sheetState = rememberModalBottomSheetState(
    animationSpec = SpringSpec(dampingRatio = 10F, stiffness = 100_000F),
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true,
  )
  val bottomSheetNavigator = remember { BottomSheetNavigator(sheetState = sheetState) }

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

  val currentDestination = navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)
  val destinationStyle = currentDestination.value?.destination()?.style
  val isBackgroundBlurred = destinationStyle is DestinationStyle.Dialog

  AppScreenContent(
    state = appState.value,
    isBackgroundBlurred = isBackgroundBlurred,
    navigator = navigator,
  )

  LaunchedEffect(Unit) {
    Coil.setImageLoader(imageLoader)

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
