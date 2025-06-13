package io.chefbook.ui.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.chefbook.ui.utils.compose.modifiers.clippingBackground
import io.chefbook.ui.utils.compose.modifiers.clickable.simpleClickable
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.icons.Users

@Composable
fun ProfileAvatar(
  url: String?,
  modifier: Modifier = Modifier,
  size: Dp = 144.dp,
  strokeWidth: Dp = size / 40,
  strokeBrush: Brush? = null,
  onClick: () -> Unit = {},
) {
  val colors = LocalTheme.colors

  var processedModifier = modifier
    .size(size)
    .clip(CircleShape)

  if (strokeBrush != null) {
    processedModifier = processedModifier
      .border(
        BorderStroke(
          brush = strokeBrush,
          width = strokeWidth,
        ),
        shape = CircleShape
      )
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = processedModifier
      .simpleClickable(onClick = onClick)
      .padding(all = if (strokeBrush != null) strokeWidth * 2 else 0.dp)
      .clippingBackground(colors.backgroundPrimary, CircleShape)
  ) {
    if (url.isNullOrBlank()) {
      Icon(
        imageVector = ChefBookIcons.Users,
        contentDescription = null,
        tint = colors.foregroundSecondary,
        modifier = Modifier.size(size / 1.5F),
      )
    }
    AsyncImage(
      model = ImageRequest.Builder(LocalPlatformContext.current)
        .data(url)
        .crossfade(true)
        .build(),
      contentDescription = null,

      contentScale = ContentScale.Crop,
      colorFilter = if (url.isNullOrBlank()) ColorFilter.tint(colors.foregroundPrimary) else null,
    )
  }
}