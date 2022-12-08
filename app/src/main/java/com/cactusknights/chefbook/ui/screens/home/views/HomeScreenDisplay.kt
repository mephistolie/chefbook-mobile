package com.cactusknights.chefbook.ui.screens.home.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.ui.navigation.hosts.HomeHost
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import com.cactusknights.chefbook.ui.screens.home.models.HomeState
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import kotlinx.coroutines.launch
import kotlin.math.pow

private val minimalSheetPeekHeight = 1.dp

private val tabsContentHeight = 44.dp
private val tabsTopPadding = 4.dp
private val tabsBottomPadding = 12.dp
private val tabsHeight = tabsContentHeight + tabsTopPadding + tabsBottomPadding

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenDisplay(
    appState: AppState,
    appController: NavHostController,
    homeState: HomeState,
    homeController: NavHostController,
    onEvent: (HomeEvent) -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val colors = ChefBookTheme.colors

    val coroutineScope = rememberCoroutineScope()

    val peekHeight = remember { Animatable(0.dp, Dp.VectorConverter) }

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val progress = progressBySheet(sheetState, sheetState.isAnimationRunning)

    val startBackground = colors.backgroundSecondary
    val endBackground = colors.backgroundPrimary
    val animatedBackground = animatedColor(startBackground, endBackground, progress)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(animatedBackground)
            .statusBarsPadding()
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                val targetValue = with(density) { coordinates.size.height.toDp() - tabsHeight }
                coroutineScope.launch { if (targetValue != peekHeight.targetValue) peekHeight.animateTo(targetValue) }
            },
        sheetShape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
        sheetBackgroundColor = colors.backgroundPrimary,
        sheetElevation = 0.dp,
        sheetPeekHeight = if (peekHeight.value > 0.dp) peekHeight.value else minimalSheetPeekHeight,
        sheetContent = {
            HomeHost(
                defaultTab = appState.settings?.defaultTab ?: Tab.RECIPE_BOOK,
                appController = appController,
                dashboardController = homeController,
                sheetProgress = progress,
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedBackground)
        ) {
            Row(
                modifier = Modifier
                    .background(animatedBackground)
                    .statusBarsPadding()
                    .padding(16.dp, tabsTopPadding, 16.dp, 0.dp)
                    .fillMaxWidth()
                    .height(tabsContentHeight)
                    .alpha(1F - progress),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            (4 * progress).dp,
                            (4 * progress).dp,
                            0.dp,
                            0.dp
                        )
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DynamicButton(
                        onClick = { onEvent(HomeEvent.OpenRecipeBook) },
                        modifier = Modifier.size(44.dp),
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_recipes),
                        isSelected = homeState.currentTab == Tab.RECIPE_BOOK,
                        selectedForeground = colors.foregroundPrimary,
                        selectedBackground = colors.backgroundTertiary,
                        unselectedForeground = colors.foregroundPrimary,
                        iconsSize = 24.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DynamicButton(
                        onClick = { onEvent(HomeEvent.OpenShoppingList) },
                        modifier = Modifier.size(44.dp),
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_list),
                        isSelected = homeState.currentTab == Tab.SHOPPING_LIST,
                        selectedForeground = colors.foregroundPrimary,
                        selectedBackground = colors.backgroundTertiary,
                        unselectedForeground = colors.foregroundPrimary,
                        iconsSize = 24.dp
                    )
                }
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                if (appState.profile.avatar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(appState.profile.avatar)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_user_circle),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user_circle),
                        contentDescription = null,
                        tint = colors.foregroundPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(
                                0.dp,
                                0.dp,
                                (4 * progress).dp,
                                (4 * progress).dp,
                            )
                    )
                }
            }
        }
    }

    LaunchedEffect(homeState.currentTab) {
        val animation = keyframes {
            durationMillis = 400
            if (sheetState.isCollapsed) {
                peekHeight.targetValue at 0
                0.dp at 125
                0.dp at 275
                peekHeight.targetValue at 400
            }
        }

        launch {
            peekHeight.animateTo(
                targetValue = peekHeight.targetValue,
                animationSpec = animation
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun progressBySheet(state: BottomSheetState, isAnimationRunning: Boolean) =
    when {
        isAnimationRunning -> 0F
        state.direction > 0F -> 1F - state.progress.fraction
        state.direction < 0F && (state.progress.fraction != 1F || state.currentValue != state.targetValue) -> state.progress.fraction
        else ->
            if (state.isCollapsed) {
                0F
            } else {
                1F
            }
    }

fun animatedColor(start: Color, end: Color, progress: Float): Color {
    val red = start.red + progress.pow(2) * (end.red - start.red)
    return Color(red, red, red)
}
