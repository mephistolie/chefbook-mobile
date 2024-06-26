package io.chefbook.features.community.recipes.ui.screens.filter.components.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

@Composable
internal fun OptionButton(
  name: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .wrapContentHeight()
      .scalingClickable(pressed = pressed, scaleFactor = 0.9F, onClick = onClick),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    content()
    Spacer(Modifier.height(4.dp))
    Text(
      text = name,
      style = if (isSelected) typography.subhead1 else typography.caption1,
      textAlign = TextAlign.Center,
      color = if (isSelected) colors.foregroundPrimary else colors.foregroundSecondary,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
  }
}
