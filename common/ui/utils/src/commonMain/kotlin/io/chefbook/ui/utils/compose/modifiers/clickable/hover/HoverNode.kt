package io.chefbook.ui.utils.compose.modifiers.clickable.hover

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import io.chefbook.ui.utils.compose.constants.Hidden
import io.chefbook.ui.utils.compose.constants.Visible
import kotlinx.coroutines.launch

internal class HoverNode(
  private val interactionSource: InteractionSource,
  var color: Color,
) : Modifier.Node(), DrawModifierNode {

  override val shouldAutoInvalidate: Boolean = false

  private val alpha = Animatable(Hidden)

  override fun onAttach() {
    coroutineScope.launch {
      interactionSource.interactions
        .collect { interaction ->
          when (interaction) {
            is PressInteraction.Press -> animateTo(Visible)

            is PressInteraction.Release,
            is PressInteraction.Cancel -> animateTo(Hidden)
          }
        }
    }
  }

  override fun ContentDrawScope.draw() {
    drawContent()
    drawRect(
      color = color,
      alpha = alpha.value,
    )
  }

  private suspend fun animateTo(value: Float) {
    alpha.animateTo(value, spring())
  }
}
