package io.chefbook.ui.screens.main

import android.content.res.Resources
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import io.chefbook.navigation.hosts.AppHost
import io.chefbook.navigation.navigators.AppNavigator
import io.chefbook.ui.screens.main.mvi.AppState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import io.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.colors.Monochrome7
import io.chefbook.features.shoppinglist.control.ui.screen.destinations.ShoppingListScreenDestination
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList

private val blackBackgroundModals =
  listOf(RecipeScreenDestination.route)

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun AppScreenContent(
  state: AppState,
  navigator: AppNavigator,
  isBackgroundBlurred: Boolean = false
) {
  val resources = LocalContext.current.resources

  val blurRadius = animateDpAsState(
    label = "blur_radius",
    targetValue = if (isBackgroundBlurred) 20.dp else 0.dp,
  )

  ChefBookTheme(darkTheme = isDarkTheme(state.theme, resources)) {
    val colors = LocalTheme.colors

    ModalBottomSheetLayout(
      modifier = Modifier
        .background(colors.backgroundPrimary)
        .blur(
          radius = blurRadius.value,
          edgeTreatment = BlurredEdgeTreatment.Unbounded,
        ),
      bottomSheetNavigator = navigator.bottomSheet,
      sheetShape = RectangleShape,
      sheetBackgroundColor = Transparent,
      sheetElevation = 0.dp,
      scrimColor = if (navigator.navController.currentDestination?.route in blackBackgroundModals) {
        Color.Black
      } else {
        ModalBottomSheetDefaults.scrimColor
      },
    ) {
      AppHost(navigator = navigator)
    }

    AppThemeLaunchedEffect(
      theme = state.theme,
      currentDestination = navigator.navController.currentDestination,
    )
  }
}

@Composable
@NonRestartableComposable
private fun AppThemeLaunchedEffect(
  theme: AppTheme,
  currentDestination: NavDestination?,
) {
  val context = LocalContext.current
  val colors = LocalTheme.colors

  LaunchedEffect(key1 = theme, key2 = currentDestination) {
    val route = currentDestination?.route
    val isBlackBackgroundModal = route in blackBackgroundModals
    val isDarkStatusBar = if (isBlackBackgroundModal) true else colors.isDark

    (context as? ComponentActivity)?.enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.auto(
        android.graphics.Color.TRANSPARENT,
        android.graphics.Color.TRANSPARENT,
      ) { isDarkStatusBar },
      navigationBarStyle = SystemBarStyle.auto(
        Color.White.toArgb(),
        Monochrome7.toArgb(),
      ) { colors.isDark },
    )
  }
}

private fun isDarkTheme(
  theme: AppTheme,
  resources: Resources
) =
  when (theme) {
    AppTheme.LIGHT -> false
    AppTheme.DARK -> true
    AppTheme.SYSTEM -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      resources.configuration.isNightModeActive
    } else {
      false
    }
  }
