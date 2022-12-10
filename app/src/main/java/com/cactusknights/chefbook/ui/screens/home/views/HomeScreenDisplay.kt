package com.cactusknights.chefbook.ui.screens.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.ui.navigation.hosts.HomeHost
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import com.cactusknights.chefbook.ui.screens.home.models.HomeState
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.bottomsheet.AnimatedBottomSheet
import com.cactusknights.chefbook.ui.views.bottomsheet.rememberAnimatedBottomSheetState
import kotlinx.coroutines.launch
import kotlin.math.pow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenDisplay(
    appState: AppState,
    appController: NavHostController,
    homeState: HomeState,
    homeController: NavHostController,
    onEvent: (HomeEvent) -> Unit,
) {
    val sheetState = rememberAnimatedBottomSheetState()
    val expandProgress = sheetState.expandProgress
    val animatedBackground = animatedBackground(expandProgress, homeState.currentTab)

    AnimatedBottomSheet(
        sheetState = sheetState,
        modifier = Modifier
            .background(animatedBackground)
            .statusBarsPadding()
            .fillMaxSize(),
        sheetShape = RoundedCornerShape(28.dp, 28.dp, 0.dp, 0.dp),
        sheetContent = {
            HomeHost(
                defaultTab = appState.settings?.defaultTab ?: Tab.RECIPE_BOOK,
                appController = appController,
                dashboardController = homeController,
                expandProgress = expandProgress,
            )
        },
    ) {
        TopBar(
            currentTab = homeState.currentTab,
            avatar = appState.profile.avatar,
            onEvent = onEvent,
            expandProgress = expandProgress,
            modifier = Modifier
                .wrapContentHeight()
                .background(animatedBackground)
        )
    }

    LaunchedEffect(homeState.currentTab) {
        launch { sheetState.reopen() }
    }
}

@Composable
fun animatedBackground(progress: Float, tab: Tab): Color {
    val colors = ChefBookTheme.colors
    if (tab == Tab.SHOPPING_LIST) return colors.backgroundSecondary
    val gray = colors.backgroundSecondary.red + progress.pow(2) * (colors.backgroundPrimary.red - colors.backgroundSecondary.red)
    return Color(gray, gray, gray)
}
