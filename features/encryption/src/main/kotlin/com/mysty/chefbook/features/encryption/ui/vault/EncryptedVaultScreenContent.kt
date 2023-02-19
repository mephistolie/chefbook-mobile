package com.mysty.chefbook.features.encryption.ui.vault

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
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.CircleIconButton
import com.mysty.chefbook.features.encryption.R
import com.mysty.chefbook.features.encryption.ui.vault.components.EncryptedVaultScreenManagement
import com.mysty.chefbook.features.encryption.ui.vault.components.EncryptedVaultScreenPinCode
import com.mysty.chefbook.features.encryption.ui.vault.components.EncryptedVaultScreenPresentation
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenIntent
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenState
import com.mysty.chefbook.features.encryption.ui.vault.mvi.PinCodeInputType

@OptIn(ExperimentalComposeUiApi::class)
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
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
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
