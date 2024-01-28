package io.chefbook.features.recipe.input.ui.screens.cooking.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape12
import io.chefbook.design.R as designR

@Composable
internal fun StepAddPictureButton(
  onAddPictureClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  Box(
    modifier = modifier
      .width(64.dp)
      .clippedBackground(colors.backgroundSecondary, RoundedCornerShape12)
      .aspectRatio(1.5F)
      .simpleClickable(onClick = onAddPictureClick),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_image),
      tint = colors.foregroundPrimary,
      modifier = Modifier.size(24.dp),
      contentDescription = null,
    )
  }
}
