package com.cactusknights.chefbook.ui.screens.main.views

import android.content.res.Resources
import android.os.Build
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.mysty.chefbook.api.settings.domain.entities.Theme
import com.cactusknights.chefbook.ui.navigation.hosts.RootHost
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.cactusknights.chefbook.ui.screens.main.models.UiState
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.shapes.ModalBottomSheetShape

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppScreenDisplay(
    state: AppState,
    navController: NavHostController,
    bottomSheetNavigator: BottomSheetNavigator,
    sheetState: ModalBottomSheetState,
) {
    val settings = state.settings
    val resources = LocalContext.current.resources

    val backgroundBlurState = UiState.backgroundBlur.collectAsState()
    val transition = updateTransition(backgroundBlurState, label = "backgroundBlurState")
    val backgroundBlur by transition.animateDp(label = "backgroundBlur") { blur -> blur.value }

    if (settings != null) {
        ChefBookTheme(
            darkTheme = isDarkTheme(settings.theme, resources)
        ) {
            val colors = LocalTheme.colors

            val systemUiController = rememberSystemUiController()

            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = ModalBottomSheetShape,
                sheetBackgroundColor = colors.backgroundPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPrimary)
                    .blur(backgroundBlur)
            ) {
                RootHost(
                    settings = settings,
                    appState = state,
                    navController = navController,
                    sheetState = sheetState,
                )
            }

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Transparent,
                    darkIcons = !colors.isDark,
                    isNavigationBarContrastEnforced = false,
                )
            }
        }
    }
}

private fun isDarkTheme(theme: Theme, resources: Resources) =
    when (theme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resources.configuration.isNightModeActive
        } else {
            false
        }
    }
