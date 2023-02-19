package com.mysty.chefbook.features.recipe.input.ui.screens.details.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.textfields.ThemedIndicatorTextField
import com.mysty.chefbook.features.recipe.input.R

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
        stringResource(R.string.common_general_description),
        color = colors.foregroundPrimary
      )
    },
  )
}