package io.chefbook.features.about.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.R as coreR
import io.chefbook.features.about.R

@Composable
internal fun AboutInfo(
  versionName: String,
  versionCode: Long,
  buildType: String,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Image(
      imageVector = ImageVector.vectorResource(coreR.drawable.ic_broccy),
      contentDescription = null,
      modifier = Modifier
        .padding(top = 16.dp)
        .size(128.dp),
    )
    Text(
      text = stringResource(R.string.common_about_screen_version, versionName),
      style = typography.body2,
      color = colors.foregroundSecondary,
      modifier = Modifier.padding(top = 8.dp)
    )
    Text(
      text = stringResource(R.string.common_about_screen_build, versionCode, buildType),
      style = typography.body2,
      color = colors.foregroundSecondary,
      modifier = Modifier.padding(bottom = 12.dp)
    )
  }
}
