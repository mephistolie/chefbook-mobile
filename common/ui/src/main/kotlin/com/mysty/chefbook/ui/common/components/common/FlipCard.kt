package com.mysty.chefbook.ui.common.components.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import com.mephistolie.compost.modifiers.simpleClickable
import kotlin.math.absoluteValue

private const val ANGLE_0 = 0F
private const val ANGLE_90 = 90F
private const val ANGLE_180 = 180F

@ExperimentalMaterialApi
@Composable
fun FlippingCard(
    modifier: Modifier = Modifier,
    isFrontShown: Boolean = true,
    useVerticalAxis: Boolean = false,
    onClick: (Boolean) -> Unit = {},
    backContent: @Composable BoxScope.() -> Unit = {},
    frontContent: @Composable BoxScope.() -> Unit = {},
) {
    val rotation = animateFloatAsState(
        targetValue = if (isFrontShown) ANGLE_0 else ANGLE_180,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )
    val scale = 0.9F + 0.1F * ((rotation.value - ANGLE_90) / ANGLE_90).absoluteValue
    Box(
        modifier = modifier
            .scale(scale)
            .simpleClickable { onClick(isFrontShown) }
            .graphicsLayer {
                if (useVerticalAxis) rotationX = rotation.value else rotationY = rotation.value
                cameraDistance = 48F * density
            },
    ) {
        if (rotation.value <= ANGLE_90) {
            Box(modifier = Modifier.fillMaxSize()) { frontContent() }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (useVerticalAxis) rotationX = ANGLE_180 else rotationY = ANGLE_180
                    },
            ) {
                backContent()
            }
        }
    }
}