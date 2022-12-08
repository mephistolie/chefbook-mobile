package com.cactusknights.chefbook.ui.screens.recipe.views.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenTab
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsBlock(
    tabs: List<RecipeScreenTab>,
    tabsBlockHeight: MutableState<Dp>,
    columnState: LazyListState,
    pagerState: PagerState,
) {
    val density = LocalDensity.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val coroutine = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colors.backgroundPrimary,
        divider = {
            Divider(
                color = colors.backgroundSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = colors.tintPrimary,
                height = 2.dp,
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .clip(RoundedCornerShape(1.dp)),
            )
        },
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .padding(horizontal = 12.dp)
            .onSizeChanged { size ->
                tabsBlockHeight.value = (size.height / density.density).dp
            },
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == pagerState.currentPage,
                selectedContentColor = colors.backgroundPrimary,
                unselectedContentColor = colors.backgroundPrimary,
                onClick = {
                    coroutine.launch {
                        if (index != pagerState.currentPage) {
                            pagerState.animateScrollToPage(index)
                        } else {
                            columnState.animateScrollToItem(3)
                        }
                    }
                },
            ) {
                Text(
                    text = stringResource(id = tab.nameId),
                    style = typography.headline2,
                    color = if (index == pagerState.currentPage) {
                        colors.foregroundPrimary
                    } else {
                        colors.foregroundSecondary
                    },
                    maxLines = 1,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}
