package io.chefbook.core.compose.modifiers.clickable.scale

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.node.DelegatableNode

const val DefaultPressedScale = 0.96F

internal class PressedScaleIndication(
  private val scale: Float,
) : IndicationNodeFactory {

  override fun create(interactionSource: InteractionSource): DelegatableNode = PressedScaleNode(
    interactionSource = interactionSource,
    pressedScale = scale,
  )

  override fun hashCode() = scale.hashCode()

  override fun equals(other: Any?): Boolean {
    if (other !is PressedScaleIndication) return false

    return scale == other.scale
  }
}

fun scaleIndication(
  scale: Float = DefaultPressedScale,
): Indication = PressedScaleIndication(scale)
