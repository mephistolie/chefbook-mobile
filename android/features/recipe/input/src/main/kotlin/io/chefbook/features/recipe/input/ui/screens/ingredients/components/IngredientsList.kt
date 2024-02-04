package io.chefbook.features.recipe.input.ui.screens.ingredients.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputIngredientsScreenIntent
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState

internal fun LazyListScope.ingredientsList(
  ingredients: List<IngredientsItem>,
  reorderableState: ReorderableLazyListState,
  haptic: HapticFeedback,
  onIntent: (RecipeInputIngredientsScreenIntent) -> Unit,
) {
  items(items = ingredients, key = { item -> item.id }) { item ->
    ReorderableItem(reorderableState, key = item.id) { isDragging ->
      if (isDragging) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
      when (item) {
        is IngredientsItem.Section -> {
          SectionField(
            name = item.name,
            onNameChange = { name ->
              onIntent(
                RecipeInputIngredientsScreenIntent.SetIngredientItemName(item.id, name)
              )
            },
            onDeleteClick = {
              onIntent(RecipeInputIngredientsScreenIntent.DeleteIngredientItem(item.id))
            },
          )
        }
        is IngredientsItem.Ingredient -> {
          IngredientField(
            ingredient = item,
            onInputClick = {
              onIntent(RecipeInputIngredientsScreenIntent.OpenIngredientDialog(item.id))
            },
            onDeleteClick = {
              onIntent(RecipeInputIngredientsScreenIntent.DeleteIngredientItem(item.id))
            },
          )
        }
        else -> Unit
      }
    }
  }
}