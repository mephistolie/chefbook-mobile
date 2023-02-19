package com.cactusknights.chefbook.ui.screens.main

import android.content.res.Resources
import android.os.Build
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.navigation.hosts.AppHost
import com.cactusknights.chefbook.navigation.navigators.AppNavigator
import com.cactusknights.chefbook.ui.screens.main.mvi.AppState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mysty.chefbook.api.settings.domain.entities.Theme
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.shapes.ModalBottomSheetShape

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun AppScreenContent(
  state: AppState,
  navigator: AppNavigator,
  isBackgroundBlurred: Boolean = false
) {
    val resources = LocalContext.current.resources

    val blur = animateDpAsState(targetValue = if (isBackgroundBlurred) 20.dp else 0.dp)

    ChefBookTheme(darkTheme = isDarkTheme(state.theme, resources)) {
        val colors = LocalTheme.colors
        val systemUiController = rememberSystemUiController()

        ModalBottomSheetLayout(
            modifier = Modifier
                .background(colors.backgroundPrimary)
                .blur(blur.value),
            bottomSheetNavigator = navigator.bottomSheet,
            sheetShape = ModalBottomSheetShape,
            sheetBackgroundColor = colors.backgroundPrimary,
        ) {
            AppHost(navigator = navigator)
        }

        LaunchedEffect(Unit) {
            systemUiController.setSystemBarsColor(
                color = Transparent,
                darkIcons = !colors.isDark,
                isNavigationBarContrastEnforced = false,
            )
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
