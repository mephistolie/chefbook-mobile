package io.chefbook.ui.utils.compose.modifiers.clickable.scale

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement

private data class PressedScaleElement(
  private val interactionSource: InteractionSource,
  private val scale: Float,
) : ModifierNodeElement<PressedScaleNode>() {
  override fun create() = PressedScaleNode(interactionSource, scale)

  override fun update(node: PressedScaleNode) {
    node.pressedScale = scale
  }
}

fun Modifier.pressedScale(
  interactionSource: InteractionSource,
  scale: Float = DefaultPressedScale,
) = this then PressedScaleElement(interactionSource, scale)
