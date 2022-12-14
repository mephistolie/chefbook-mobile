package com.cactusknights.chefbook.ui.screens.encryptedvault.views

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenEvent
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenState
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.PinCodeInputType
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.CircleIconButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EncryptedVaultScreenDisplay(
    state: EncryptedVaultScreenState,
    onEvent: (EncryptedVaultScreenEvent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

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
                iconId = R.drawable.ic_cross,
                onClick = {
                    keyboardController?.hide()
                    onEvent(EncryptedVaultScreenEvent.Close)
                },
                modifier = Modifier
                    .padding(top = 18.dp, end = 6.dp)
                    .size(28.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
                tint = Color.White
            )
            if (state is EncryptedVaultScreenState.PinCodeInput && state.type == PinCodeInputType.VALIDATION) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                    tint = colors.foregroundSecondary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 6.dp, top = 18.dp)
                        .size(28.dp)
                        .padding(2.dp)
                        .aspectRatio(1F)
                        .simpleClickable { onEvent(EncryptedVaultScreenEvent.Back) },
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
