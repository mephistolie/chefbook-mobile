package io.chefbook.ui.screens.main

import android.content.res.Resources
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.theme.ChefBookTheme
import io.chefbook.ui.design.theme.colors.Monochrome7
import io.chefbook.features.auth.ui.AuthScreen
import io.chefbook.features.root.RootComponent
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.ui.screens.main.mvi.RootState

@Composable
fun RootScreenContent(
  state: RootState,
  component: RootComponent,
) {
  val resources = LocalContext.current.resources

  ChefBookTheme(darkTheme = isDarkTheme(state.theme, resources)) {
    AppThemeLaunchedEffect(theme = state.theme)

    val child = component.child.subscribeAsState()
    val childInstance = child.value.child?.instance

    when (childInstance) {
      is RootComponent.Child.SignedIn -> SignedInContent(component = childInstance.component)
      is RootComponent.Child.SignedOut -> AuthScreen(component = childInstance.component)
      null -> Unit
    }
  }
}

@Composable
@NonRestartableComposable
private fun AppThemeLaunchedEffect(
  theme: AppTheme,
) {
  val context = LocalContext.current
  val colors = LocalTheme.colors

  LaunchedEffect(theme) {
    (context as? ComponentActivity)?.enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.auto(
        lightScrim = android.graphics.Color.TRANSPARENT,
        darkScrim = android.graphics.Color.TRANSPARENT,
        detectDarkMode = { colors.isDark },
      ),
      navigationBarStyle = SystemBarStyle.auto(
        lightScrim = Color.White.toArgb(),
        darkScrim = Monochrome7.toArgb(),
        detectDarkMode = { colors.isDark },
      ),
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
