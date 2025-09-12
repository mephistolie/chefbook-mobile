package io.chefbook.features.root.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.chefbook.ui.design.theme.ChefBookTheme
import io.chefbook.features.auth.ui.AuthScreen
import io.chefbook.features.root.component.RootComponent
import io.chefbook.features.root.ui.mvi.RootState
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme

@Composable
fun RootScreenContent(
  state: RootState,
  component: RootComponent,
) {
  ChefBookTheme(darkTheme = isDarkTheme(state.theme)) {
//    AppThemeLaunchedEffect(theme = state.theme)

    val child = component.child.subscribeAsState()
    val childInstance = child.value.child?.instance

    when (childInstance) {
      is RootComponent.Child.SignedIn -> SignedInContent(component = childInstance.component)
      is RootComponent.Child.SignedOut -> AuthScreen(component = childInstance.component)
      null -> Unit
    }
  }
}

//@Composable
//@NonRestartableComposable
//private fun AppThemeLaunchedEffect(
//  theme: AppTheme,
//) {
//  val context = LocalContext.current
//  val colors = LocalTheme.colors
//
//  LaunchedEffect(theme) {
//    (context as? ComponentActivity)?.enableEdgeToEdge(
//      statusBarStyle = SystemBarStyle.auto(
//        lightScrim = android.graphics.Color.TRANSPARENT,
//        darkScrim = android.graphics.Color.TRANSPARENT,
//        detectDarkMode = { colors.isDark },
//      ),
//      navigationBarStyle = SystemBarStyle.auto(
//        lightScrim = Color.White.toArgb(),
//        darkScrim = Monochrome7.toArgb(),
//        detectDarkMode = { colors.isDark },
//      ),
//    )
//  }
//}

@Composable
private fun isDarkTheme(theme: AppTheme) =
  when (theme) {
    AppTheme.LIGHT -> false
    AppTheme.DARK -> true
    AppTheme.SYSTEM -> isSystemInDarkTheme()
  }
