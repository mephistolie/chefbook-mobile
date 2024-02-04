package io.chefbook.features.recipebook.dashboard.ui.components.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.features.recipebook.dashboard.R
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenIntent
import io.chefbook.design.R as designR

@Composable
internal fun DashboardTopBar(
  modifier: Modifier = Modifier,
  avatar: String? = null,
  onIntent: (DashboardScreenIntent) -> Unit,
) {
  val context = LocalContext.current
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = ImageRequest.Builder(context)
          .data(avatar)
          .crossfade(true)
          .build(),
        placeholder = painterResource(designR.drawable.ic_user_circle),
        error = painterResource(designR.drawable.ic_user_circle),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        colorFilter = if (avatar.isNullOrBlank()) ColorFilter.tint(LocalTheme.colors.foregroundPrimary) else null,
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .simpleClickable { onIntent(DashboardScreenIntent.OpenProfile) }
      )
      Text(
        stringResource(R.string.common_dashboard_screen_home),
        modifier = Modifier.padding(start = 12.dp),
        style = typography.h2,
        color = colors.foregroundPrimary,
      )
    }
    Icon(
      painter = painterResource(id = designR.drawable.ic_add),
      tint = colors.tintPrimary,
      contentDescription = null,
      modifier = Modifier
        .size(28.dp)
        .simpleClickable(1000L) { onIntent(DashboardScreenIntent.OpenNewRecipeInput) }
    )
  }
}
