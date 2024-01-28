package io.chefbook.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.chefbook.core.android.compose.providers.theme.ThemeProvider
import io.chefbook.design.theme.colors.palettes.DarkPalette
import io.chefbook.design.theme.colors.palettes.LightPalette
import io.chefbook.design.theme.typography.Typography

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
