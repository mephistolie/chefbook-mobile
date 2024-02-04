package io.chefbook.ui.common.components.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R

@Composable
fun ProfileAvatar(
  url: String?,
  modifier: Modifier = Modifier,
  size: Dp = 144.dp,
  strokeWidth: Dp = size / 40,
  strokeBrush: Brush? = null,
  onClick: () -> Unit = {},
) {
  val context = LocalContext.current

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
    modifier = processedModifier.simpleClickable(onClick = onClick)
  ) {
    if (url.isNullOrBlank()) {
      Icon(
        painter = painterResource(id = R.drawable.ic_user),
        contentDescription = null,
        tint = colors.foregroundSecondary,
        modifier = Modifier.size(size / 2),
      )
    }
    AsyncImage(
      model = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .build(),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      colorFilter = if (url.isNullOrBlank()) ColorFilter.tint(colors.foregroundPrimary) else null,
      modifier = Modifier
        .padding(all = if (strokeBrush != null) strokeWidth * 2 else 0.dp)
        .clip(CircleShape)
    )
  }
}