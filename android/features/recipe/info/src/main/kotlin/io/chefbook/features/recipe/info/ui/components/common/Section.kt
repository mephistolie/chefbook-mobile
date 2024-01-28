package io.chefbook.features.recipe.info.ui.components.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
internal fun Section(
  name: String,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Text(
    text = name,
    style = typography.h3,
    color = colors.foregroundSecondary,
    modifier = modifier,
  )
}
