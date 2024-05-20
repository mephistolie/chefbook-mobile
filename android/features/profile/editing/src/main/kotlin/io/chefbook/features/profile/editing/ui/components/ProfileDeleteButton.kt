package io.chefbook.features.profile.editing.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ComponentBigHeight
import io.chefbook.features.profile.editing.R

@Composable
@NonRestartableComposable
fun ProfileDeleteButton(
  onClick: () -> Unit,
) {
  val colors = LocalTheme.colors

  DynamicButton(
    onClick = onClick,
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .fillMaxWidth()
      .height(ComponentBigHeight),
    text = stringResource(R.string.common_profile_editing_screen_delete_profile),
    unselectedForeground = colors.tintPrimary,
    cornerRadius = 20.dp,
  )
}