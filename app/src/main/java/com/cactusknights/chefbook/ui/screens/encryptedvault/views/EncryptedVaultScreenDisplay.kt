package com.cactusknights.chefbook.ui.screens.encryptedvault.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenEvent
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.ui.buttons.CircleIconButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EncryptedVaultScreenDisplay(
    state: EncryptedVaultScreenState,
    onEvent: (EncryptedVaultScreenEvent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
        ) {
            Text(
                text = stringResource(R.string.common_global_encryption),
                maxLines = 1,
                style = typography.h4,
                color = colors.foregroundPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
            )
            CircleIconButton(
                icon = ImageVector.vectorResource(R.drawable.ic_cross),
                onClick = {
                    keyboardController?.hide()
                    onEvent(EncryptedVaultScreenEvent.Close)
                },
                modifier = Modifier
                    .padding(top = 18.dp, end = 6.dp)
                    .size(28.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
                tint = ChefBookTheme.colors.backgroundPrimary
            )
        }
        Divider(
            color = colors.backgroundTertiary,
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
                    onCreateVaultButtonClick = { onEvent(EncryptedVaultScreenEvent.CreateVault) }
                )
            }
            is EncryptedVaultScreenState.PinCodeInput -> {
                EncryptedVaultScreenPinCode(
                    state = state,
                    onPinCodeNumAdd = { number -> onEvent(EncryptedVaultScreenEvent.AddPinCodeNum(number)) },
                    onPinCodeNumRemove = { onEvent(EncryptedVaultScreenEvent.RemovePinCodeNum) }
                )
            }
            EncryptedVaultScreenState.Management -> {
                EncryptedVaultScreenManagement(
                    onLockVaultButtonClick = { onEvent(EncryptedVaultScreenEvent.LockVault) },
                    onChangePinCodeVaultClick = { onEvent(EncryptedVaultScreenEvent.ChangePinCode) },
                    onDeleteVaultClick = { onEvent(EncryptedVaultScreenEvent.DeleteVault) },
                )
            }
        }
    }

}
