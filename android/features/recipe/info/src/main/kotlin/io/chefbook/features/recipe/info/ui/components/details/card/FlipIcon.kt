package io.chefbook.features.recipe.info.ui.components.details.card

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.dimens.MediumIconSize
import io.chefbook.design.R as designR

@Composable
internal fun BoxScope.FlipIcon() {
  val colors = LocalTheme.colors
  val backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)

  Icon(
    imageVector = ImageVector.vectorResource(designR.drawable.ic_flip),
    contentDescription = null,
    modifier = Modifier
      .align(Alignment.BottomEnd)
      .padding(8.dp)
      .size(MediumIconSize)
      .clippedBackground(backgroundColor, shape = CircleShape)
      .padding(4.dp),
    tint = Color.White,
  )
}