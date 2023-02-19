package com.mysty.chefbook.features.recipe.info.ui.components.details.diet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.macronutrients.MacronutrientsInfo
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.recipe.info.R

@Composable
internal fun DietWidget(
  calories: Int?,
  macronutrients: MacronutrientsInfo?,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(modifier = modifier) {
    Text(
      text = stringResource(R.string.common_general_in_100_g),
      modifier = Modifier.padding(bottom = 2.dp),
      style = typography.caption1,
      color = colors.foregroundSecondary
    )
    Row {
      DietElement(
        name = stringResource(R.string.common_general_kcal),
        value = calories,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
      DietElement(
        name = stringResource(R.string.common_general_protein),
        value = macronutrients?.protein,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
      DietElement(
        name = stringResource(R.string.common_general_fats),
        value = macronutrients?.fats,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
      DietElement(
        name = stringResource(R.string.common_general_carbs),
        value = macronutrients?.carbohydrates,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
      )
    }
  }
}
