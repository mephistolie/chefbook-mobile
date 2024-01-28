package io.chefbook.features.recipe.info.ui.components.details.info

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
internal fun InfoElement(
  name: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.Start
  ) {
    Text(
      text = name,
      style = typography.subhead1,
      color = colors.foregroundSecondary,
    )
    Text(
      text = value,
      style = typography.headline1,
      color = colors.foregroundPrimary,
    )
  }
}