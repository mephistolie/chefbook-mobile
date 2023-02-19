package com.mysty.chefbook.design.components.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    color: Color = LocalTheme.colors.tintPrimary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
) {
    androidx.compose.material.CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth,
    )
}
