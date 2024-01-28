package io.chefbook.features.recipe.input.ui.screens.details.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.ThemedIndicatorTextField
import io.chefbook.core.android.R as coreR

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
        stringResource(coreR.string.common_general_name),
        color = colors.foregroundPrimary
      )
    },
    maxLines = 1,
  )
}