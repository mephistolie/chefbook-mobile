package com.mysty.chefbook.design.components.dividers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.dimens.DividerHeight

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = LocalTheme.colors.backgroundSecondary,
    height: Dp = DividerHeight,
) {
    androidx.compose.material.Divider(
        color = color,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    )
}
