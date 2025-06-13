package io.chefbook.ui.utils.compose.modifiers

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Creates clipped background instead of standard [androidx.compose.foundation.background] that
 * applies shape only visually.
 *
 * @param background color used as the background
 * @param shape shape for clipping
 */
fun Modifier.clippingBackground(
  color: Color,
  shape: Shape,
): Modifier = this
  .clip(shape)
  .background(color, shape)
