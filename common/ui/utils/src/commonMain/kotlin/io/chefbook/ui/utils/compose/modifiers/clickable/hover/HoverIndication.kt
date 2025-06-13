package io.chefbook.ui.utils.compose.modifiers.clickable.hover

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.DelegatableNode

val DefaultHoverColor = Color.Black.copy(alpha = 0.08F)

internal class HoverIndication(
  private val color: Color,
) : IndicationNodeFactory {

  override fun create(interactionSource: InteractionSource): DelegatableNode = HoverNode(
    interactionSource = interactionSource,
    color = color,
  )

  override fun hashCode() = color.hashCode()

  override fun equals(other: Any?): Boolean {
    if (other !is HoverNode) return false

    return color == other.color
  }
}

fun hoverIndication(
  color: Color = DefaultHoverColor,
): Indication = HoverIndication(color)
