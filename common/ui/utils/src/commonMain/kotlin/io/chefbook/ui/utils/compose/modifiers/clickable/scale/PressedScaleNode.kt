package io.chefbook.ui.utils.compose.modifiers.clickable.scale

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ReleasedScale = 1F

internal class PressedScaleNode(
  private val interactionSource: InteractionSource,
  var pressedScale: Float,
) : Modifier.Node(), DrawModifierNode {

  override val shouldAutoInvalidate: Boolean = false

  private val animation = Animatable(ReleasedScale)

  private var animateJob: Job? = null

  override fun onAttach() {
    coroutineScope.launch {
      interactionSource.interactions
        .collectLatest { interaction ->
          when (interaction) {
            is PressInteraction.Press -> animateTo(pressedScale)

            is PressInteraction.Release,
            is PressInteraction.Cancel -> {
              animateTo(ReleasedScale)
            }
          }
        }
    }
  }

  override fun ContentDrawScope.draw() {
    scale(scale = animation.value) {
      this@draw.drawContent()
    }
  }

  private fun animateTo(value: Float) {
    animateJob?.cancel()
    animateJob = coroutineScope.launch {
      animation.animateTo(value, spring(stiffness = Spring.StiffnessMediumLow))
    }
  }
}
