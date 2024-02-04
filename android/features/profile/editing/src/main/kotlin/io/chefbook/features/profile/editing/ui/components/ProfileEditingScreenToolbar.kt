package io.chefbook.features.profile.editing.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun ProfileEditingScreenToolbar(
  isConfirmButtonAvailable: Boolean,
  onBackClick: () -> Unit,
  onConfirmClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Toolbar(
    modifier = Modifier
      .background(colors.backgroundPrimary)
      .statusBarsPadding()
      .padding(horizontal = 12.dp),
    onLeftButtonClick = onBackClick,
    rightButtonIconId = designR.drawable.ic_check,
    rightButtonTint = if (isConfirmButtonAvailable) colors.tintPrimary else colors.backgroundSecondary,
    onRightButtonClick = { if (isConfirmButtonAvailable) onConfirmClick() }
  ) {
    Text(
      text = stringResource(coreR.string.common_general_profile_editing),
      style = typography.h4,
      color = colors.foregroundPrimary,
    )
  }
}
