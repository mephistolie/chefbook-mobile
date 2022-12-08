package com.cactusknights.chefbook.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightPalette = ChefBookColors(
    backgroundPrimary = Color.White,
    backgroundSecondary = Monochrome96,
    backgroundTertiary = Monochrome92,

    foregroundPrimary = Monochrome30,
    foregroundSecondary = Monochrome60,

    isDark = false,
)

private val DarkPalette = ChefBookColors(
    backgroundPrimary = Monochrome7,
    backgroundSecondary = Monochrome11,
    backgroundTertiary = Monochrome20,

    foregroundPrimary = Monochrome85,
    foregroundSecondary = Monochrome75,

    isDark = true
)

@Composable
fun ChefBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkPalette else LightPalette
    val typography = Typography


    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content
    )
}

object ChefBookTheme {
    val colors: ChefBookColors
        @Composable
        get() = LocalColors.current
    val typography: ChefBookTypography
        @Composable
        get() = LocalTypography.current
}

private val LocalColors = staticCompositionLocalOf<ChefBookColors> {
    error("No ChefBook Palette provided")
}

private val LocalTypography = staticCompositionLocalOf<ChefBookTypography> {
    error("No ChefBook Typography provided")
}