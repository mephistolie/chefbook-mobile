package com.mysty.chefbook.core.android.compose.providers.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.BlendMode
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme

@Composable
fun ThemeProvider(
    colors: Colors,
    typography: Typography,
    content: @Composable () -> Unit
)  {
    val shimmerTheme = defaultShimmerTheme.copy(blendMode = BlendMode.DstOut)
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalShimmerTheme provides shimmerTheme,
        content = content
    )
}

object LocalTheme {
    val colors: Colors
        @Composable
        get() = LocalColors.current
    val typography: Typography
        @Composable
        get() = LocalTypography.current
}

internal val LocalColors = staticCompositionLocalOf<Colors> {
    error("No ChefBook Palette provided")
}

internal val LocalTypography = staticCompositionLocalOf<Typography> {
    error("No ChefBook Typography provided")
}
