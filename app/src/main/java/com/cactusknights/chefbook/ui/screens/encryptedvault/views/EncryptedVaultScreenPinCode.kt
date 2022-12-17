package com.cactusknights.chefbook.ui.screens.encryptedvault.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenState
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.PinCodeInputType
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.textfields.PinCodeField
import com.mysty.chefbook.design.theme.ChefBookTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EncryptedVaultScreenPinCode(
    state: EncryptedVaultScreenState.PinCodeInput,
    onPinCodeNumAdd: (Int) -> Unit,
    onPinCodeNumRemove: () -> Unit,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val focusRequesters = remember {
        Array(EncryptedVaultScreenState.PIN_CODE_LENGTH) { FocusRequester() }
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(
                id = if (state.type != PinCodeInputType.VALIDATION)
                    R.string.common_encrypted_vault_screen_pin_code
                else
                    R.string.common_encrypted_vault_screen_pin_code_repeat
            ),
            modifier = Modifier
                .padding(top = 48.dp, bottom = 32.dp),
            color = colors.foregroundPrimary,
            textAlign = TextAlign.Center,
            style = typography.h4,
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .simpleClickable { keyboardController?.show() }
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 24.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            repeat(EncryptedVaultScreenState.PIN_CODE_LENGTH) { index ->
                PinCodeField(
                    value = state.pinCode.getOrNull(index),
                    onPinCodeNumAdd = { number -> onPinCodeNumAdd(number) },
                    onPinCodeNumRemove = onPinCodeNumRemove,
                    modifier = Modifier
                        .focusRequester(focusRequesters[index]),
                    isFocused = state.pinCode.length == index,
                )
            }
        }

        LaunchedEffect(state.pinCode) {
            focusRequesters[state.pinCode.length].requestFocus()
        }

        LocalView.current.viewTreeObserver.addOnWindowFocusChangeListener {
            if (it) focusRequesters[state.pinCode.length].requestFocus()
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightEncryptedVaultScreenPinCode() = ThemedEncryptedVaultScreenPinCode(Strings.EMPTY, false)

@Composable
@Preview(showBackground = true)
private fun PreviewDarkEncryptedVaultScreenPinCode() = ThemedEncryptedVaultScreenPinCode("123", true)

@Composable
private fun ThemedEncryptedVaultScreenPinCode(
    pinCode: String,
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = LocalTheme.colors.backgroundPrimary
        ) {
            EncryptedVaultScreenPinCode(
                state = EncryptedVaultScreenState.PinCodeInput(
                    type = PinCodeInputType.CREATION,
                    pinCode = pinCode,
                ),
                onPinCodeNumAdd = {},
                onPinCodeNumRemove = {},
            )
        }
    }
}
