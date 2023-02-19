package com.mysty.chefbook.features.recipe.info.ui.components.cooking

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.core.android.utils.minutesToTimeString
import com.mysty.chefbook.core.utils.TimeUtils
import com.mysty.chefbook.design.theme.dimens.ButtonSmallHeight

@Composable
internal fun ServingsBlock(
  time: Int,
) {
  val resources = LocalContext.current.resources

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(ButtonSmallHeight),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = TimeUtils.minutesToTimeString(
        minutes = time,
        resources = resources
      ),
      style = typography.headline1,
      color = colors.foregroundSecondary,
    )
  }
  Divider(
    color = colors.backgroundSecondary,
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp)
      .height(1.dp)
  )
}
