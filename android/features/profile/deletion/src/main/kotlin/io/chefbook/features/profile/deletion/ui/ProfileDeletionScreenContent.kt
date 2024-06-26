package io.chefbook.features.profile.deletion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.components.checkboxes.Checkbox
import io.chefbook.design.components.spacers.VerticalSpacer
import io.chefbook.design.components.textfields.OutlinedTextField
import io.chefbook.design.theme.dimens.ComponentMediumHeight
import io.chefbook.features.profile.deletion.R
import io.chefbook.features.profile.deletion.ui.mvi.ProfileDeletionScreenIntent
import io.chefbook.features.profile.deletion.ui.mvi.ProfileDeletionScreenState
import io.chefbook.ui.common.dialogs.StandardDialog
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun ProfileDeletionScreenContent(
  state: ProfileDeletionScreenState,
  onIntent: (ProfileDeletionScreenIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  StandardDialog(
    title = stringResource(R.string.common_profile_deletion_screen_profile_deletion),
  ) {
    OutlinedTextField(
      value = state.password,
      onValueChange = { text -> onIntent(ProfileDeletionScreenIntent.SetPassword(text)) },
      modifier = modifier.fillMaxWidth(),
      hint = stringResource(coreR.string.common_general_password),
      confidential = true,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
      modifier = Modifier
        .simpleClickable { onIntent(ProfileDeletionScreenIntent.SwitchSharedDataDeletionStatus) },
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Checkbox(
        isChecked = state.deleteSharedData,
        isEnabled = false,
        onClick = {}
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = stringResource(R.string.common_profile_deletion_screen_delete_shared_data),
        style = typography.headline2,
        color = colors.foregroundPrimary
      )
    }
    VerticalSpacer(24.dp)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      DynamicButton(
        leftIcon = ImageVector.vectorResource(designR.drawable.ic_cross),
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
          .weight(1F)
          .fillMaxWidth()
          .height(ComponentMediumHeight),
        onClick = { onIntent(ProfileDeletionScreenIntent.Close) },
      )
      DynamicButton(
        leftIcon = ImageVector.vectorResource(designR.drawable.ic_check),
        isSelected = state.password.isNotEmpty(),
        isEnabled = state.password.isNotEmpty(),
        modifier = Modifier
          .weight(1F)
          .fillMaxWidth()
          .height(ComponentMediumHeight),
        onClick = { onIntent(ProfileDeletionScreenIntent.RequestProfileDeletion) },
      )
    }
  }
}
