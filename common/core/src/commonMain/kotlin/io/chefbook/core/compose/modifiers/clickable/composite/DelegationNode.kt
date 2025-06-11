package io.chefbook.core.compose.modifiers.clickable.composite

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.node.DelegatingNode

internal class DelegationNode(
  private val interactionSource: InteractionSource,
  factories: List<IndicationNodeFactory>,
) : DelegatingNode() {

  init {
    factories.forEach { factory ->
      val node = factory.create(interactionSource)
      delegate(node)
    }
  }
}
