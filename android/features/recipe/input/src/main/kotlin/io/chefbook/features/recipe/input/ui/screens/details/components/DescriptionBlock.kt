package io.chefbook.features.recipe.input.ui.screens.details.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.IndicatorLineTextField
import io.chefbook.core.res as CoreR

@Composable
internal fun DescriptionBlock(
  description: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  ThemedIndicatorTextField(
    value = description,
    modifier = modifier,
    onValueChange = onValueChange,
    label = {
      Text(
        stringResource(CoreR.string.common_general_description),
        color = colors.foregroundPrimary
      )
    },
  )
}