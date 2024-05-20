package io.chefbook.features.recipe.info.ui.components.details.card

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape24
import io.chefbook.features.recipe.info.ui.components.details.diet.DietWidget
import io.chefbook.features.recipe.info.ui.components.details.info.InfoWidget
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState

@Composable
internal fun BoxScope.DetailsCard(
  state: RecipeScreenState.Success,
  showFlipIcon: Boolean = false,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val recipe = state.recipe

  Column(
    modifier = Modifier
      .fillMaxSize()
      .clippedBackground(colors.backgroundPrimary, RoundedCornerShape24)
      .padding(horizontal = 12.dp)
  ) {
    Text(
      text = state.recipe.name,
      modifier = Modifier.padding(top = 16.dp),
      maxLines = 3,
      style = typography.h2,
      color = colors.foregroundPrimary,
    )
    if (recipe.hasDietData) {
      DietWidget(
        calories = recipe.calories,
        macronutrients = recipe.macronutrients,
        modifier = Modifier.padding(top = 16.dp)
      )
    }
    InfoWidget(
      recipe = state.recipe,
      modifier = Modifier.padding(top = 16.dp)
    )
  }
  if (showFlipIcon) FlipIcon()
}
