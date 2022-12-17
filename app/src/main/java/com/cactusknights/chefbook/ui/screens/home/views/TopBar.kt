package com.cactusknights.chefbook.ui.screens.home.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

private val tabsContentHeight = 44.dp
private val tabsTopPadding = 4.dp
private val tabsBottomPadding = 12.dp

@Composable
fun TopBar(
    currentTab: Tab,
    avatar: String?,
    onEvent: (HomeEvent) -> Unit,
    expandProgress: Float,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(16.dp, tabsTopPadding, 16.dp, 0.dp)
            .fillMaxWidth()
            .height(tabsContentHeight)
            .alpha(1F - expandProgress),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    (4 * expandProgress).dp,
                    (4 * expandProgress).dp,
                    0.dp,
                    0.dp
                )
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TabButton(
                onClick = { onEvent(HomeEvent.OpenRecipeBook) },
                iconId = R.drawable.ic_recipes,
                isSelected = currentTab == Tab.RECIPE_BOOK,
            )
            Spacer(modifier = Modifier.width(2.dp))
            TabButton(
                onClick = { onEvent(HomeEvent.OpenShoppingList) },
                iconId = R.drawable.ic_list,
                isSelected = currentTab == Tab.SHOPPING_LIST,
            )
        }
        Spacer(
            modifier = Modifier.width(8.dp)
        )
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
                .padding(
                    0.dp,
                    0.dp,
                    (4 * expandProgress).dp,
                    (4 * expandProgress).dp,
                )
                .size(24.dp)
                .clip(CircleShape)
                .simpleClickable { onEvent(HomeEvent.OpenProfile) }
        )
    }
}
