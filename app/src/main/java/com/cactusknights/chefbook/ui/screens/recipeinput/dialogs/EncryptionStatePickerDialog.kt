package com.cactusknights.chefbook.ui.screens.recipeinput.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.elements.RadioElement
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

@Composable
fun EncryptionStatePickerDialog(
    viewModel: RecipeInputScreenViewModel,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val viewModelState = viewModel.state.collectAsState()
    val isEncrypted = viewModelState.value.input.isEncrypted

    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
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
        Divider(
            color = colors.backgroundSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        RadioElement(
            icon = ImageVector.vectorResource(R.drawable.ic_lock_open),
            name = stringResource(R.string.common_general_standard),
            description = stringResource(R.string.common_recipe_input_screen_standard_description),
            isSelected = !isEncrypted,
            onSelected = {
                viewModel.obtainEvent(RecipeInputScreenEvent.SetEncryptedState(false))
            },
        )
        RadioElement(
            icon = ImageVector.vectorResource(R.drawable.ic_lock),
            name = stringResource(R.string.common_general_encrypted),
            description = stringResource(R.string.common_recipe_input_screen_encrypted_description),
            isSelected = isEncrypted,
            onSelected = {
                viewModel.obtainEvent(RecipeInputScreenEvent.SetEncryptedState(true))
            },
        )
        Spacer(modifier = Modifier.height(72.dp))
    }
}