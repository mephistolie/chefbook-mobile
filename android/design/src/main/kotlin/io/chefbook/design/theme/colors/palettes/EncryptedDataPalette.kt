package io.chefbook.design.theme.colors.palettes

import androidx.compose.ui.graphics.Color
import io.chefbook.core.android.compose.providers.theme.Colors
import io.chefbook.design.theme.colors.DeepOrangeDark
import io.chefbook.design.theme.colors.DeepOrangeLight

internal val EncryptedDataPalette = Colors(
  tintPrimary = DeepOrangeLight,
  tintSecondary = DeepOrangeDark,

  backgroundPrimary = Color(0xFF10101A),
  backgroundSecondary = Color(0xFF1E1E33),
  backgroundTertiary = Color(0xFF9A8C98),

  foregroundPrimary = Color(0xFFF2E9E4),
  foregroundSecondary = Color(0xFFE5C9C3),

  isDark = true
)
