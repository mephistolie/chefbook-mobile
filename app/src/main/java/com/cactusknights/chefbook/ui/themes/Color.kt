package com.cactusknights.chefbook.ui.themes

import androidx.compose.ui.graphics.Color

data class ChefBookColors(
    val tintPrimary: Color = DeepOrangeLight,
    val tintSecondary: Color = DeepOrangeDark,

    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,

    val foregroundPrimary: Color,
    val foregroundSecondary: Color,

    val isDark: Boolean
)

val Monochrome96 = Color(0xFFF6F6F6)
val Monochrome95 = Color(0xFFF2F2F2)
val Monochrome85 = Color(0xFFDADADA)
val Monochrome75 = Color(0xFFC0C0C0)
val Monochrome60 = Color(0xFF999999)
val Monochrome30 = Color(0xFF4C4C4C)
val Monochrome20 = Color(0xFF333333)
val Monochrome11 = Color(0xFF1C1C1C)
val Monochrome7 = Color(0xFF121212)

val DeepOrangeLight = Color(0xFFFF8A50)
val DeepOrangeDark = Color(0xFF754027)

val Red = Color(0xFFFF5252)