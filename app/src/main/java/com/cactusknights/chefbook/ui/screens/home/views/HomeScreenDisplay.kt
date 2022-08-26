package com.cactusknights.chefbook.ui.screens.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.simpleClickable
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.navigation.hosts.HomeHost
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import kotlin.math.pow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenDisplay(
    appState: AppState,
    appController: NavHostController,
    dashboardController: NavHostController,
    onEvent: (HomeEvent) -> Unit,
) {
    val density = LocalDensity.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    var screenHeight by remember { mutableStateOf(0F) }
    var sheetHeight by rememberSaveable { mutableStateOf(0F) }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val progress = progressBySheet(sheetState)

    val startBackground = colors.backgroundSecondary
    val endBackground = colors.backgroundPrimary
    val animatedBackground = animatedColor(startBackground, endBackground, progress)

    var isTabMenuExpanded by remember { mutableStateOf(false) }
    var tabButtonCoordinates by remember { mutableStateOf(Offset(0F, 0F)) }

    val positionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ) = IntOffset((tabButtonCoordinates.x - 8F).toInt(), (tabButtonCoordinates.y + 32F).toInt())
    }

    val context = LocalContext.current

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(animatedBackground)
            .fillMaxSize()
            .statusBarsPadding()
            .onSizeChanged {
                if (screenHeight == 0F) {
                    screenHeight = it.height.toFloat()
                }
            },
        sheetShape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
        sheetBackgroundColor = colors.backgroundPrimary,
        sheetElevation = 0.dp,
        sheetPeekHeight = (screenHeight / density.density).dp - (sheetHeight / density.density).dp + 8.dp,
        sheetContent = {
            HomeHost(
                defaultTab = appState.settings?.defaultTab ?: Tab.RECIPE_BOOK,
                appController = appController,
                dashboardController = dashboardController,
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
                    .statusBarsPadding()
                    .padding(18.dp, 16.dp, 22.dp, 24.dp)
                    .fillMaxWidth()
                    .height(24.dp)
                    .onGloballyPositioned {
                        if (sheetHeight == 0F) {
                            sheetHeight = it.boundsInRoot().bottom
                        }
                    }
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
                        .simpleClickable { isTabMenuExpanded = !isTabMenuExpanded }
                        .weight(1f)
                        .onGloballyPositioned { coordinates ->
                            if (tabButtonCoordinates.x == 0F) {
                                tabButtonCoordinates = coordinates.boundsInRoot().bottomLeft
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (dashboardController.currentDestination?.route == Destination.Home.RecipeBook.route) {
                            stringResource(id = R.string.common_general_recipes)
                        } else {
                            stringResource(id = R.string.common_general_purchases)
                        },
                        style = typography.h3,
                        color = colors.foregroundPrimary,
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = null,
                        tint = colors.foregroundSecondary,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(0.dp, 4.dp, 0.dp, 0.dp)
                    )
                    if (isTabMenuExpanded && progress == 0F) {
                        Popup(
                            popupPositionProvider = positionProvider,
                            onDismissRequest = { isTabMenuExpanded = false }
                        ) {
                            val popupInteractionSource = remember { MutableInteractionSource() }
                            Column(
                                modifier = Modifier
                                    .shadow(24.dp, shape = RoundedCornerShape(12.dp))
                                    .background(
                                        colors.backgroundPrimary,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .width(176.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = popupInteractionSource,
                                            onClick = {
                                                onEvent(HomeEvent.OpenRecipeBook)
                                                isTabMenuExpanded = false
                                            },
                                            indication = null,
                                        )
                                        .padding(16.dp, 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.common_general_recipes),
                                        style = typography.headline1,
                                        color = colors.foregroundPrimary,
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_recipes),
                                        contentDescription = null,
                                        tint = colors.foregroundPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Divider()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = popupInteractionSource,
                                            onClick = {
                                                onEvent(HomeEvent.OpenShoppingList)
                                                isTabMenuExpanded = false
                                            },
                                            indication = null,
                                        )
                                        .padding(16.dp, 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.common_general_purchases),
                                        style = typography.headline1,
                                        color = colors.foregroundPrimary,
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_list),
                                        contentDescription = null,
                                        tint = colors.foregroundPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            }
                        }
                    }

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
}

@OptIn(ExperimentalMaterialApi::class)
fun progressBySheet(state: BottomSheetState) =
    when {
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
    return Color(red, red, red,)
}
