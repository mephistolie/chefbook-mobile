package io.chefbook.features.recipe.input.ui.screens.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.counter.Counter
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.core.android.R as coreR

@Composable
internal fun ServingsBlock(
  state: RecipeInput,
  onSetServings: (Int?) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = stringResource(coreR.string.common_general_servings),
      style = typography.headline1,
      color = colors.foregroundPrimary,
    )
    Counter(
      count = state.servings ?: 0,
      isTextEditable = true,
      onValueChange = { value -> onSetServings(value.toIntOrNull()) },
      onMinusClicked = { onSetServings((state.servings ?: 0) - 1) },
      onPlusClicked = { onSetServings((state.servings ?: 0) + 1) },
    )
  }
}