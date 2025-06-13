package io.chefbook.features.auth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.icons.Broccy
import io.chefbook.ui.design.icons.ChefBookIcons

@Composable
fun ChefBookLogo(
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      imageVector = ChefBookIcons.Broccy,
      contentDescription = null,
      modifier = Modifier.size(40.dp)
    )
    Spacer(Modifier.width(8.dp))
    Text(
      text = "Chef",
      style = typography.h1,
      color = colors.tintPrimary
    )
    Text(
      text = "Book",
      style = typography.h1,
      color = colors.foregroundPrimary
    )
  }
}
