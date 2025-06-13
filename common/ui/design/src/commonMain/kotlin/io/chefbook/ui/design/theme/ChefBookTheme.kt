package io.chefbook.ui.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.providers.theme.ThemeProvider
import io.chefbook.ui.design.theme.colors.palettes.DarkPalette
import io.chefbook.ui.design.theme.colors.palettes.LightPalette
import io.chefbook.ui.design.theme.typography.chefBookTypography

@Composable
fun ChefBookTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) =
  ThemeProvider(
    colors = if (darkTheme) DarkPalette else LightPalette,
    typography = chefBookTypography,
    content = content,
  )
