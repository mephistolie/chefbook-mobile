package com.mysty.chefbook.features.recipe.info.ui.components.common.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.bottomsheet.BottomSheetSlider

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun RecipeTabsHeader(
    tabsBlockHeight: MutableState<Dp>,
    pagerState: PagerState,
    onTabDoubleClick: () -> Unit,
) {
    val colors = LocalTheme.colors

    Column(
        modifier = Modifier.background(colors.backgroundPrimary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BottomSheetSlider()
        RecipeTabs(
            tabsBlockHeight = tabsBlockHeight,
            pagerState = pagerState,
            onTabDoubleClick = onTabDoubleClick,
        )
    }
}
