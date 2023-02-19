package com.mysty.chefbook.features.recipe.info.ui.components.ingredients

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.recipe.info.ui.components.common.Section

internal fun LazyListScope.ingredientsList(
  ingredients: List<IngredientItem>,
  selectedIngredients: Set<String>,
  servingsMultiplier: Int,
  servings: Int? = null,
  onIngredientClick: (String) -> Unit,
) {

  val amountRatio = servingsMultiplier.toFloat() / (servings?.toFloat()
    ?: servingsMultiplier.toFloat())

  items(
    count = ingredients.size,
    key = { index -> ingredients[index].id }
  ) { index ->
    when (val item = ingredients[index]) {
      is IngredientItem.Section -> {
        Section(item.name)
        Spacer(modifier = Modifier.height(12.dp))
      }
      is IngredientItem.Ingredient -> {
        Ingredient(
          ingredient = item,
          isChecked = selectedIngredients.contains(item.id),
          amountRatio = amountRatio,
          modifier = Modifier.simpleClickable { onIngredientClick(item.id) }
        )
        if (index + 1 < ingredients.size && ingredients[index + 1] is IngredientItem.Section) {
          Divider(
            color = LocalTheme.colors.backgroundSecondary,
            modifier = Modifier
              .padding(top = 18.dp, bottom = 12.dp)
              .fillMaxWidth()
              .height(1.dp)
          )
        } else {
          Spacer(modifier = Modifier.height(18.dp))
        }
      }
      else -> Unit
    }
  }
}