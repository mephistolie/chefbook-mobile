package com.mysty.chefbook.features.recipe.info.ui.components.ingredients

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem

@Composable
internal fun IngredientsPage(
  ingredients: List<IngredientItem>,
  selectedIngredients: Set<String>,
  servingsMultiplier: Int,
  servings: Int? = null,
  onServingsChanged: (Int) -> Unit,
  onIngredientClick: (String) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp)
      .wrapContentHeight()
  ) {
    item { Spacer(modifier = Modifier.height(12.dp)) }
    val hasDynamicIngredients = ingredients.any { it is IngredientItem.Ingredient && it.amount != null }
    if (servings != null || hasDynamicIngredients) {
      item {
        ServingsBlock(
          servingsMultiplier = servingsMultiplier,
          servings = servings,
          hasDynamicIngredients = hasDynamicIngredients,
          onServingsChanged = onServingsChanged,
        )
      }
    }
    ingredientsList(
      ingredients = ingredients,
      selectedIngredients = selectedIngredients,
      servingsMultiplier = servingsMultiplier,
      servings = servings,
      onIngredientClick = onIngredientClick,
    )
  }
}
