package io.chefbook.features.about.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R as designR

@Composable
internal fun AboutScreenButton(
  titleId: Int,
  onClick: () -> Unit
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = Modifier
      .padding(horizontal = 12.dp, vertical = 16.dp)
      .fillMaxWidth()
      .wrapContentHeight()
      .simpleClickable(onClick = onClick),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = stringResource(titleId),
      style = typography.headline1,
      color = colors.foregroundPrimary,
      modifier = Modifier.wrapContentHeight()
    )
    Icon(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_right),
      tint = colors.foregroundSecondary,
      modifier = Modifier.size(18.dp),
      contentDescription = null,
    )
  }
}
