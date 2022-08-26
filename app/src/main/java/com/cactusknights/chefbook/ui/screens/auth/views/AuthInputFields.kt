package com.cactusknights.chefbook.ui.screens.auth.views

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.views.textfields.FilledTextField

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun EmailInputField(
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    FilledTextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        hint = stringResource(R.string.common_general_email),
        icon = Icons.Rounded.Email,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Email,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        confidential = false
    )
}

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
fun PasswordInputField(
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    hint: String = stringResource(R.string.common_general_password),
    imeAction: ImeAction = ImeAction.Done,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    FilledTextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        hint = hint,
        icon = Icons.Outlined.Lock,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        confidential = true
    )
}