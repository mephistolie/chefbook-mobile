package io.chefbook.features.recipe.info.ui.components.details.card

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
internal fun BoxScope.ImageCard(
  url: String,
  modifier: Modifier = Modifier,
  showFlipIcon: Boolean = false,
) {
  val colors = LocalTheme.colors


  if (showFlipIcon) FlipIcon()
}
