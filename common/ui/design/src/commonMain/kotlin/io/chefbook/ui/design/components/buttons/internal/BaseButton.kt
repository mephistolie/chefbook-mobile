package io.chefbook.ui.design.components.buttons.internal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.chefbook.ui.utils.compose.modifiers.clippingBackground
import io.chefbook.ui.utils.compose.constants.ShortDebounceInterval
import io.chefbook.ui.utils.compose.modifiers.clickable.composite.hoverScaleIndication
import io.chefbook.ui.utils.compose.modifiers.clickable.debounceClickable
import io.chefbook.ui.utils.compose.modifiers.clickable.hover.hoverIndication
import io.chefbook.ui.utils.compose.modifiers.clickable.scale.pressedScale
import io.chefbook.ui.design.components.buttons.ButtonColors
import io.chefbook.ui.design.theme.shapes.SmoothCornerShape16

@Composable
internal fun BaseButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = SmoothCornerShape16,
  colors: ButtonColors,
  border: BorderStroke? = null,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = hoverScaleIndication(),
  debounceInterval: Long? = ShortDebounceInterval,
  horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
  content: @Composable RowScope.(InteractionSource) -> Unit,
) {
  val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

  Row(
    modifier = modifier
      .pressedScale(interactionSource)
      .clippingBackground(color = colors.containerColor, shape = shape)
      .run {
        if (border == null) return@run this
        border(border, shape)
      }
      .debounceClickable(
        interactionSource = interactionSource,
        indication = hoverIndication(),
        enabled = enabled,
        debounceInterval = debounceInterval,
        onClick = onClick,
      )
      .padding(contentPadding),
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CompositionLocalProvider(
      LocalContentColor provides colors.contentColor,
    ) {
      content(interactionSource)
    }
  }
}
