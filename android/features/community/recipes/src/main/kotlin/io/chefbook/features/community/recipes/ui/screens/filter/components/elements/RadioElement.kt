package io.chefbook.features.community.recipes.ui.screens.filter.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.radibuttons.RadioButton

@Composable
fun RadioElement(
  text: String,
  isSelected: Boolean,
  onSelected: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = Modifier
      .padding(vertical = 16.dp)
      .fillMaxWidth()
      .wrapContentHeight()
      .simpleClickable(onClick = onSelected),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
      text = text,
      style = typography.body1,
      color = colors.foregroundPrimary,
    )
    RadioButton(
      isSelected = isSelected,
      onClick = {},
      isEnabled = false,
    )
  }
}