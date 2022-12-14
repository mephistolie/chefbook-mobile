package com.mysty.chefbook.design.theme.colors.palettes

import androidx.compose.ui.graphics.Color
import com.mysty.chefbook.core.ui.compose.providers.theme.Colors
import com.mysty.chefbook.design.theme.colors.DeepOrangeDark
import com.mysty.chefbook.design.theme.colors.DeepOrangeLight
import com.mysty.chefbook.design.theme.colors.Monochrome30
import com.mysty.chefbook.design.theme.colors.Monochrome60
import com.mysty.chefbook.design.theme.colors.Monochrome92
import com.mysty.chefbook.design.theme.colors.Monochrome96

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
