package io.chefbook.features.recipe.info.ui.components.ingredients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.R as coreR
import io.chefbook.design.components.counter.Counter
import io.chefbook.design.theme.dimens.ButtonSmallHeight
import io.chefbook.features.recipe.info.R

@Composable
internal fun ServingsBlock(
  servingsMultiplier: Int,
  servings: Int? = null,
  hasDynamicIngredients: Boolean,
  onServingsChanged: (Int) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(ButtonSmallHeight),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = when {
          !hasDynamicIngredients && servings != null -> {
            stringResource(R.string.common_recipe_screen_servings_count, servings)
          }
          servings != null -> stringResource(coreR.string.common_general_servings)
          else -> stringResource(coreR.string.common_general_multiplier)
        },
        style = typography.headline1,
        color = colors.foregroundSecondary,
      )
    }
    if (hasDynamicIngredients) {
      Counter(
        count = servingsMultiplier,
        onMinusClicked = { onServingsChanged(-1) },
        onPlusClicked = { onServingsChanged(+1) },
        isMultiplier = servings == null,
      )
    }
  }
  Divider(
    color = colors.backgroundSecondary,
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp)
      .height(1.dp)
  )
}
