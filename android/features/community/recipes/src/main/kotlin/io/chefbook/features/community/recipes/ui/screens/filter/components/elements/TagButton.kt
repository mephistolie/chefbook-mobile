package io.chefbook.features.community.recipes.ui.screens.filter.components.elements

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

@Composable
internal fun TagButton(
  tag: Tag,
  isSelected: Boolean,
  onSelected: (String) -> Unit,
  onUnselected: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  OptionButton(
    modifier = modifier
      .wrapContentHeight()
      .scalingClickable(pressed, scaleFactor = 0.9F) {
        if (!isSelected) onSelected(tag.id) else onUnselected(tag.id)
      },
    name = tag.name,
    isSelected = isSelected,
    onClick = { if (!isSelected) onSelected(tag.id) else onUnselected(tag.id) },
  ) {
    DynamicButton(
      text = tag.emoji.orEmpty(),
      textStyle = typography.h1,
      isSelected = isSelected || pressed.value,
      selectedBackground = colors.foregroundPrimary,
      unselectedBackground = colors.backgroundSecondary,
      onClick = {},
      modifier = Modifier.size(60.dp),
    )
  }
}
