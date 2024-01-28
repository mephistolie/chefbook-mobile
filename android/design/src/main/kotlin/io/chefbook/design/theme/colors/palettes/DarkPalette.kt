package io.chefbook.design.theme.colors.palettes

import io.chefbook.core.android.compose.providers.theme.Colors
import io.chefbook.design.theme.colors.DeepOrangeDark
import io.chefbook.design.theme.colors.DeepOrangeLight
import io.chefbook.design.theme.colors.Monochrome11
import io.chefbook.design.theme.colors.Monochrome20
import io.chefbook.design.theme.colors.Monochrome7
import io.chefbook.design.theme.colors.Monochrome75
import io.chefbook.design.theme.colors.Monochrome85

internal val DarkPalette = Colors(
  tintPrimary = DeepOrangeLight,
  tintSecondary = DeepOrangeDark,

  backgroundPrimary = Monochrome7,
  backgroundSecondary = Monochrome11,
  backgroundTertiary = Monochrome20,

  foregroundPrimary = Monochrome85,
  foregroundSecondary = Monochrome75,

  isDark = true
)
