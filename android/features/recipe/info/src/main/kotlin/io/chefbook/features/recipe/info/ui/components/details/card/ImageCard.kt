package io.chefbook.features.recipe.info.ui.components.details.card

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.images.EncryptedImage
import io.chefbook.design.theme.shapes.RoundedCornerShape24

@Composable
internal fun BoxScope.ImageCard(
  url: String,
  showFlipIcon: Boolean = false,
) {
  val colors = LocalTheme.colors

  EncryptedImage(
    data = url,
    modifier = Modifier
      .fillMaxSize()
      .clippedBackground(
        colors.backgroundPrimary,
        shape = RoundedCornerShape24
      ),
  )
  if (showFlipIcon) FlipIcon()
}
