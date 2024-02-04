package io.chefbook.features.encryption.ui.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.CircleIconButton
import io.chefbook.design.theme.shapes.ModalBottomSheetShape
import io.chefbook.design.theme.shapes.RoundedCornerShape24
import io.chefbook.features.encryption.ui.vault.components.EncryptedVaultScreenManagement
import io.chefbook.features.encryption.ui.vault.components.EncryptedVaultScreenPinCode
import io.chefbook.features.encryption.ui.vault.components.EncryptedVaultScreenPresentation
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenIntent
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenState
import io.chefbook.features.encryption.ui.vault.mvi.PinCodeInputType
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun EncryptedVaultScreenContent(
  state: EncryptedVaultScreenState,
  onIntent: (EncryptedVaultScreenIntent) -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .navigationBarsPadding()
      .imePadding()
      .padding(horizontal = if (state !is EncryptedVaultScreenState.PinCodeInput) 8.dp else 0.dp)
      .background(
        color = colors.backgroundPrimary,
        shape =
        if (state !is EncryptedVaultScreenState.PinCodeInput) RoundedCornerShape24
        else ModalBottomSheetShape
      )
      .padding(horizontal = 12.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Box(
      contentAlignment = Alignment.TopEnd,
      modifier = Modifier
    ) {
      Text(
        text = stringResource(coreR.string.common_global_encryption),
        maxLines = 1,
        style = typography.h4,
        color = colors.foregroundPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 18.dp)
      )
      CircleIconButton(
        iconId = designR.drawable.ic_cross,
        onClick = {
          keyboardController?.hide()
          onIntent(EncryptedVaultScreenIntent.Close)
        },
        modifier = Modifier
          .padding(top = 18.dp, end = 6.dp)
          .size(28.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
        tint = Color.White
      )
      if (state is EncryptedVaultScreenState.PinCodeInput && state.type == PinCodeInputType.VALIDATION) {
        Icon(
          imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_left),
          tint = colors.foregroundSecondary,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(start = 6.dp, top = 18.dp)
            .size(28.dp)
            .padding(2.dp)
            .aspectRatio(1F)
            .simpleClickable { onIntent(EncryptedVaultScreenIntent.Back) },
          contentDescription = null,
        )
      }
    }
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    when (state) {
      is EncryptedVaultScreenState.Loading -> {
        CircularProgressIndicator(
          color = colors.tintPrimary,
          modifier = Modifier
            .padding(vertical = 48.dp)
            .size(36.dp)
            .align(Alignment.CenterHorizontally)
        )
      }
      is EncryptedVaultScreenState.Presentation -> {
        EncryptedVaultScreenPresentation(
          onCreateVaultButtonClick = { onIntent(EncryptedVaultScreenIntent.CreateVault) }
        )
      }
      is EncryptedVaultScreenState.PinCodeInput -> {
        EncryptedVaultScreenPinCode(
          state = state,
          onPinCodeNumAdd = { number -> onIntent(EncryptedVaultScreenIntent.AddPinCodeNum(number)) },
          onPinCodeNumRemove = { onIntent(EncryptedVaultScreenIntent.RemovePinCodeNum) }
        )
      }
      is EncryptedVaultScreenState.Management -> {
        EncryptedVaultScreenManagement(
          onLockVaultButtonClick = { onIntent(EncryptedVaultScreenIntent.LockVault) },
          onChangePinCodeVaultClick = { onIntent(EncryptedVaultScreenIntent.ChangePinCode) },
          onDeleteVaultClick = { onIntent(EncryptedVaultScreenIntent.DeleteVault) },
        )
      }
    }
  }

}
