package io.chefbook.features.profile.control.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.ui.common.components.profile.BroccoinsBadge
import io.chefbook.ui.design.icons.ArrowStart
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.theme.dimens.IconSize24
import io.chefbook.ui.design.theme.dimens.ToolbarHeight
import io.chefbook.ui.utils.commonGeneralControl
import io.chefbook.ui.utils.compose.modifiers.clickable.simpleClickable
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import org.jetbrains.compose.resources.stringResource
import io.chefbook.ui.utils.Res as CoreRes

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
      imageVector = ChefBookIcons.ArrowStart,
      tint = colors.foregroundPrimary,
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .size(IconSize24)
        .simpleClickable(onClick = onBack),
    )
    Text(
      text = stringResource(CoreRes.string.commonGeneralControl),
      style = typography.h4,
      color = colors.foregroundPrimary,
    )
    BroccoinsBadge(
      broccoins = broccoins,
      modifier = Modifier.align(Alignment.CenterEnd),
    )
  }
}
