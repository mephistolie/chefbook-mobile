package com.mysty.chefbook.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.dimens.DefaultIconSize
import com.mysty.chefbook.features.home.R
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenIntent
import com.mysty.chefbook.ui.common.providers.LocalBottomSheetExpandProgressProvider
import kotlin.math.pow

@Composable
internal fun HomeScreenTopBar(
    currentTab: Tab,
    profileAvatar: String?,
    onIntent: (HomeScreenIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progressProvider = LocalBottomSheetExpandProgressProvider.current
    val expandProgress = progressProvider.expandProgress

    HomeScreenTopBarContent(
        currentTab = currentTab,
        avatar = profileAvatar,
        onIntent = onIntent,
        expandProgress = expandProgress,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreenTopBarContent(
    currentTab: Tab,
    avatar: String?,
    onIntent: (HomeScreenIntent) -> Unit,
    expandProgress: Float,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val animatedScale = 1F - 0.05F * expandProgress
    val animatedAlpha = 1F - expandProgress
    val animatedBackground = animatedBackground(expandProgress, currentTab)

    Row(
        modifier = modifier
            .wrapContentHeight()
            .background(animatedBackground)
            .scale(animatedScale)
            .statusBarsPadding()
            .padding(16.dp, 4.dp, 16.dp, 8.dp)
            .fillMaxWidth()
            .height(44.dp)
            .alpha(animatedAlpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeScreenTabButton(
                onClick = { onIntent(HomeScreenIntent.OpenRecipeBook) },
                iconId = R.drawable.ic_recipes,
                isSelected = currentTab == Tab.RECIPE_BOOK,
            )
            Spacer(modifier = Modifier.width(2.dp))
            HomeScreenTabButton(
                onClick = { onIntent(HomeScreenIntent.OpenShoppingList) },
                iconId = R.drawable.ic_list,
                isSelected = currentTab == Tab.SHOPPING_LIST,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(avatar)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_user_circle),
            error = painterResource(R.drawable.ic_user_circle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = if (avatar.isNullOrBlank()) ColorFilter.tint(LocalTheme.colors.foregroundPrimary) else null,
            modifier = Modifier
                .size(DefaultIconSize)
                .clip(CircleShape)
                .simpleClickable { onIntent(HomeScreenIntent.OpenProfile) }
        )
    }
}

@Composable
fun animatedBackground(progress: Float, tab: Tab): Color {
    val colors = LocalTheme.colors
    if (tab == Tab.SHOPPING_LIST) return colors.backgroundSecondary
    val gray =
        colors.backgroundSecondary.red + progress.pow(2) * (colors.backgroundPrimary.red - colors.backgroundSecondary.red)
    return Color(gray, gray, gray)
}
