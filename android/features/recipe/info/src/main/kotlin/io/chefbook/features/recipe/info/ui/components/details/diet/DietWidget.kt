package io.chefbook.features.recipe.info.ui.components.details.diet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Macronutrients
import io.chefbook.core.android.R as coreR

@Composable
internal fun DietWidget(
  calories: Int?,
  macronutrients: Macronutrients?,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(modifier = modifier) {
    Text(
      text = stringResource(coreR.string.common_general_in_100_g),
      modifier = Modifier.padding(bottom = 2.dp),
      style = typography.caption1,
      color = colors.foregroundSecondary
    )
    Row {
      DietElement(
        name = stringResource(coreR.string.common_general_kcal),
        value = calories,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
      DietElement(
        name = stringResource(coreR.string.common_general_protein),
        value = macronutrients?.protein,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
      DietElement(
        name = stringResource(coreR.string.common_general_fats),
        value = macronutrients?.fats,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
      DietElement(
        name = stringResource(coreR.string.common_general_carbs),
        value = macronutrients?.carbohydrates,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
    }
  }
}
