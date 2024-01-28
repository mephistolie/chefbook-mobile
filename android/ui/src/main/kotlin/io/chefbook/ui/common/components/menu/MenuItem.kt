package io.chefbook.ui.common.components.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R

@Composable
fun MenuItem(
  onClick: () -> Unit,
  title: String,
  modifier: Modifier = Modifier,
  @DrawableRes
  iconId: Int? = null,
  subtitle: String? = null,
  endContent: @Composable RowScope.() -> Unit = {},
  showChevron: Boolean = true,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = modifier
      .padding(vertical = 14.dp)
      .fillMaxWidth()
      .simpleClickable(onClick = onClick, debounceInterval = 400L),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      modifier = Modifier.weight(1F),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      iconId?.let {
        Icon(
          imageVector = ImageVector.vectorResource(iconId),
          contentDescription = null,
          tint = colors.foregroundPrimary,
          modifier = Modifier.size(28.dp)
        )
      }
      Column {
        Text(
          text = title,
          style = typography.h4,
          color = colors.foregroundPrimary,
        )
        subtitle?.let {
          Text(
            text = subtitle,
            style = typography.body2,
            color = colors.foregroundSecondary,
          )
        }
      }
    }
    Row(
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      endContent()
      if (showChevron) {
        Icon(
          imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
          contentDescription = null,
          tint = colors.foregroundSecondary,
          modifier = Modifier
            .size(16.dp)
        )
      }
    }
  }
}
