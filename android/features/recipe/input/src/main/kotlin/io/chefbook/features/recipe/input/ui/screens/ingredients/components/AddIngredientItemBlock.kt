package io.chefbook.features.recipe.input.ui.screens.ingredients.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputIngredientsScreenIntent
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun AddIngredientItemBlock(
  onIntent: (RecipeInputIngredientsScreenIntent) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalTheme.colors

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    DynamicButton(leftIcon = ImageVector.vectorResource(designR.drawable.ic_add),
      text = stringResource(coreR.string.common_general_section),
      cornerRadius = 12.dp,
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier.height(36.dp),
      onClick = { onIntent(RecipeInputIngredientsScreenIntent.AddIngredientSection) })
    DynamicButton(leftIcon = ImageVector.vectorResource(designR.drawable.ic_add),
      text = stringResource(coreR.string.common_general_ingredient),
      cornerRadius = 12.dp,
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier.height(36.dp),
      onClick = { onIntent(RecipeInputIngredientsScreenIntent.AddIngredient) })
  }
}
