package io.chefbook.ui.common.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.ui.design.icons.Broccoin
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme

@Composable
fun BroccoinsBadge(
  broccoins: Int,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = modifier
      .background(colors.backgroundSecondary, shape = CircleShape)
      .padding(vertical = 4.dp, horizontal = 4.dp)
      .defaultMinSize(minWidth = 32.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Image(
      imageVector = ChefBookIcons.Broccoin,
      contentDescription = null,
      modifier = Modifier.size(20.dp),
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = broccoins.toString(),
      style = typography.headline1,
      color = colors.foregroundPrimary,
      modifier = Modifier.padding(end = 6.dp)
    )
  }
}