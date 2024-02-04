package io.chefbook.features.encryption.ui.vault.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.features.encryption.R
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun EncryptedVaultScreenManagement(
  onLockVaultButtonClick: () -> Unit,
  onChangePinCodeVaultClick: () -> Unit,
  onDeleteVaultClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
      painter = painterResource(designR.drawable.ic_lock_illustration),
      contentDescription = null,
      modifier = Modifier
        .padding(top = 32.dp)
        .size(144.dp)
    )
    Text(
      text = stringResource(id = R.string.common_encrypted_vault_screen_vault_unlocked),
      modifier = Modifier
        .padding(top = 28.dp, bottom = 48.dp),
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
      style = typography.body2,
    )
    DynamicButton(
      text = stringResource(coreR.string.common_general_lock),
      isSelected = true,
      modifier = Modifier
        .padding(bottom = 24.dp)
        .fillMaxWidth()
        .height(56.dp),
      onClick = onLockVaultButtonClick,
    )
    DynamicButton(
      text = stringResource(R.string.common_encrypted_vault_screen_pin_code_change),
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      onClick = onChangePinCodeVaultClick,
    )
    DynamicButton(
      text = stringResource(coreR.string.common_general_delete),
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier
        .padding(vertical = 12.dp)
        .fillMaxWidth()
        .height(56.dp),
      onClick = onDeleteVaultClick,
    )
  }
}
