package com.cactusknights.chefbook.ui.views.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun Modifier.shimmer(
    shimmerColor: Color = if (!ChefBookTheme.colors.isDark) ChefBookTheme.colors.backgroundPrimary else  ChefBookTheme.colors.backgroundTertiary,
    backgroundColor: Color = if (!ChefBookTheme.colors.isDark) ChefBookTheme.colors.foregroundSecondary.copy(alpha = 0.15F) else ChefBookTheme.colors.backgroundPrimary,
    duration: Float = 1000F
) : Modifier {
    val shimmerColors = listOf(
        backgroundColor,
        shimmerColor,
        backgroundColor,
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = duration,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration.toInt(),
                easing = FastOutSlowInEasing
            )
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    return background(brush)
}