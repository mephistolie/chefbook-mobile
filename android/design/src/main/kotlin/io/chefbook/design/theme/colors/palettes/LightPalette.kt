package io.chefbook.design.theme.colors.palettes

import androidx.compose.ui.graphics.Color
import io.chefbook.core.android.compose.providers.theme.Colors
import io.chefbook.design.theme.colors.DeepOrangeDark
import io.chefbook.design.theme.colors.DeepOrangeLight
import io.chefbook.design.theme.colors.Monochrome30
import io.chefbook.design.theme.colors.Monochrome60
import io.chefbook.design.theme.colors.Monochrome92
import io.chefbook.design.theme.colors.Monochrome96

internal val LightPalette = Colors(
  tintPrimary = DeepOrangeLight,
  tintSecondary = DeepOrangeDark,

  backgroundPrimary = Color.White,
  backgroundSecondary = Monochrome96,
  backgroundTertiary = Monochrome92,

  foregroundPrimary = Monochrome30,
  foregroundSecondary = Monochrome60,

  isDark = false,
)
