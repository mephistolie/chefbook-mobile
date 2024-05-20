package io.chefbook.features.encryption.ui.vault.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.dimens.ComponentBigHeight
import io.chefbook.features.encryption.R
import io.chefbook.design.R as designR

@Composable
internal fun EncryptedVaultScreenPresentation(
  onCreateVaultButtonClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .navigationBarsPadding()
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
      text = stringResource(id = R.string.common_encrypted_vault_screen_description),
      modifier = Modifier
        .padding(top = 28.dp, bottom = 48.dp),
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
      style = typography.body2,
    )
    DynamicButton(
      text = stringResource(R.string.common_encrypted_vault_enable_encryption),
      isSelected = true,
      modifier = Modifier
        .fillMaxWidth()
        .height(ComponentBigHeight),
      onClick = onCreateVaultButtonClick,
    )
    Text(
      text = stringResource(id = R.string.common_encrypted_vault_screen_note),
      modifier = Modifier.padding(vertical = 12.dp),
      textAlign = TextAlign.Center,
      style = typography.caption1,
      color = colors.foregroundSecondary
    )
  }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightEncryptedVaultScreenPresentation() =
  ThemedEncryptedVaultScreenPresentation(false)

@Composable
@Preview(showBackground = true)
private fun PreviewDarkEncryptedVaultScreenPresentation() =
  ThemedEncryptedVaultScreenPresentation(true)

@Composable
private fun ThemedEncryptedVaultScreenPresentation(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      EncryptedVaultScreenPresentation(
        onCreateVaultButtonClick = {},
      )
    }
  }
}
