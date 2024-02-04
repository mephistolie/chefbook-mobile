package io.chefbook.features.profile.control.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.design.theme.dimens.ToolbarHeight
import io.chefbook.ui.common.components.profile.BroccoinsBadge
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun ProfileScreenToolbar(
  broccoins: Int,
  onBack: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(colors.backgroundPrimary)
      .statusBarsPadding()
      .height(ToolbarHeight)
      .padding(vertical = 8.dp, horizontal = 12.dp),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_left),
      tint = colors.foregroundPrimary,
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .size(DefaultIconSize)
        .simpleClickable(onClick = onBack),
    )
    Text(
      text = stringResource(coreR.string.common_general_control),
      style = typography.h4,
      color = colors.foregroundPrimary,
    )
    BroccoinsBadge(
      broccoins = broccoins,
      modifier = Modifier.align(Alignment.CenterEnd),
    )
  }
}