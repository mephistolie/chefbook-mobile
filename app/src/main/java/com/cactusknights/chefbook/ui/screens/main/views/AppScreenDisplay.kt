package com.cactusknights.chefbook.ui.screens.main.views

import android.content.res.Resources
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.domain.entities.settings.Theme
import com.cactusknights.chefbook.ui.navigation.hosts.RootHost
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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

    if (settings != null) {
        ChefBookTheme(
            darkTheme = isDarkTheme(settings.theme, resources)
        ) {
            val colors = ChefBookTheme.colors

            val systemUiController = rememberSystemUiController()

            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp),
                sheetBackgroundColor = colors.backgroundPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPrimary)
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