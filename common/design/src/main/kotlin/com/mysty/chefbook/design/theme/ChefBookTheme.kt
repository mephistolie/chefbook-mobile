package com.mysty.chefbook.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mysty.chefbook.core.ui.compose.providers.theme.ThemeProvider
import com.mysty.chefbook.design.theme.colors.palettes.DarkPalette
import com.mysty.chefbook.design.theme.colors.palettes.LightPalette
import com.mysty.chefbook.design.theme.typography.Typography

@Composable
fun ChefBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) =
    ThemeProvider(
        colors = if (darkTheme) DarkPalette else LightPalette,
        typography = Typography,
        content = content,
    )
