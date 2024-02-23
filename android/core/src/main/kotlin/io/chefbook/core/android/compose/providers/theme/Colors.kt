package io.chefbook.core.android.compose.providers.theme

import androidx.compose.ui.graphics.Color

data class Colors(
    val tintPrimary: Color,
    val tintSecondary: Color,

    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,

    val foregroundPrimary: Color,
    val foregroundSecondary: Color,

    val isDark: Boolean
) {

    val divider = if (isDark) Color.Black else backgroundSecondary
}
