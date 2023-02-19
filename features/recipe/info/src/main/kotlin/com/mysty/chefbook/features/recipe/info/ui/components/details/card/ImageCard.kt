package com.mysty.chefbook.features.recipe.info.ui.components.details.card

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mephistolie.compost.modifiers.clippedBackground
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.images.EncryptedImage
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape24

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
