package io.chefbook.core.compose.modifiers.clickable.composite

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import io.chefbook.core.compose.modifiers.clickable.hover.DefaultHoverColor
import io.chefbook.core.compose.modifiers.clickable.hover.hoverIndication
import io.chefbook.core.compose.modifiers.clickable.scale.DefaultPressedScale
import io.chefbook.core.compose.modifiers.clickable.scale.scaleIndication

internal class DelegatingIndication(
  val factories: MutableList<IndicationNodeFactory> = mutableListOf(),
) : IndicationNodeFactory {

  override fun create(interactionSource: InteractionSource): DelegatableNode =
    DelegationNode(interactionSource, factories = factories.toList())

  override fun hashCode() = factories.hashCode()

  override fun equals(other: Any?): Boolean {
    if (other !is DelegatingIndication) return false

    return factories == other.factories
  }
}

fun hoverScaleIndication(
  color: Color = DefaultHoverColor,
  scale: Float = DefaultPressedScale,
): Indication = DelegatingIndication(factories = mutableListOf(
//  hoverIndication(color) as IndicationNodeFactory,
  scaleIndication(scale) as IndicationNodeFactory,
))

operator fun Indication.plus(other: Indication): Indication =
  when {
    other !is IndicationNodeFactory -> this

    other is DelegatingIndication -> other + this

    this is DelegatingIndication -> this.also { factories.add(other) }

    else -> DelegatingIndication().apply {
      factories.add(this)
      factories.add(other)
    }
  }
