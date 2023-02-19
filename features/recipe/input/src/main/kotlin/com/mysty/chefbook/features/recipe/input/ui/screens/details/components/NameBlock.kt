package com.mysty.chefbook.features.recipe.input.ui.screens.details.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.textfields.ThemedIndicatorTextField
import com.mysty.chefbook.features.recipe.input.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun NameBlock(
  name: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  val colors = LocalTheme.colors

  ThemedIndicatorTextField(
    value = name,
    modifier = modifier,
    onValueChange = onValueChange,
    keyboardActions = KeyboardActions(
      onDone = { keyboardController?.hide() }
    ),
    label = {
      Text(
        stringResource(R.string.common_general_name),
        color = colors.foregroundPrimary
      )
    },
    maxLines = 1,
  )
}