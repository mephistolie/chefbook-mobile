package io.chefbook.features.recipe.input.ui.viewmodel

import io.chefbook.features.recipe.input.ui.mvi.RecipeInputIngredientsScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import kotlin.math.max
import kotlin.math.min

internal class RecipeInputScreenViewModelIngredientsDelegate(
  private val updateState: (block: (RecipeInputScreenState) -> RecipeInputScreenState) -> Unit,
  private val sendEffect: suspend (RecipeInputScreenEffect) -> Unit,
) {

  suspend fun reduceIntent(intent: RecipeInputIngredientsScreenIntent) {
    when (intent) {
      is RecipeInputIngredientsScreenIntent.AddIngredient ->
        addIngredientItem(IngredientsItem.Ingredient(generateUUID(), ""))
      is RecipeInputIngredientsScreenIntent.AddIngredientSection ->
        addIngredientItem(IngredientsItem.Section(generateUUID(), ""))
      is RecipeInputIngredientsScreenIntent.OpenIngredientDialog ->
        sendEffect(RecipeInputScreenEffect.Ingredients.OnDialogOpen(intent.ingredientId))
      is RecipeInputIngredientsScreenIntent.SetIngredientItemName ->
        setIngredientItemName(ingredientId = intent.ingredientId, name = intent.name)
      is RecipeInputIngredientsScreenIntent.SetIngredientAmount ->
        setIngredientAmount(ingredientId = intent.ingredientId, amount = intent.amount)
      is RecipeInputIngredientsScreenIntent.SetIngredientUnit ->
        setIngredientUnit(ingredientId = intent.ingredientId, unit = intent.unit)
      is RecipeInputIngredientsScreenIntent.MoveIngredientItem ->
        moveIngredientItem(from = intent.from, to = intent.to)
      is RecipeInputIngredientsScreenIntent.DeleteIngredientItem ->
        deleteIngredientItem(ingredientId = intent.ingredientId)
    }
  }

  private suspend fun addIngredientItem(
    item: IngredientsItem,
  ) {
    updateState { state ->
      val ingredients = state.input.ingredients.toMutableList()
      ingredients.add(item)
      state.copy(input = state.input.copy(ingredients = ingredients))
    }
  }

  private suspend fun setIngredientItemName(
    ingredientId: String,
    name: String,
  ) {
    updateState { state ->
      val ingredients = state.input.ingredients.toMutableList()
      state.copy(
        input = state.input.copy(
          ingredients = ingredients.map { item ->
            if (item.id != ingredientId) {
              item
            } else {
              when (item) {
                is IngredientsItem.Section -> item.copy(
                  name = name.substring(
                    0,
                    min(name.length, MAX_NAME_LENGTH)
                  )
                )
                is IngredientsItem.Ingredient -> item.copy(
                  name = name.substring(
                    0,
                    min(name.length, MAX_NAME_LENGTH)
                  )
                )
                else -> item
              }
            }
          })
      )
    }
  }

  private suspend fun setIngredientAmount(
    ingredientId: String,
    amount: Int?,
  ) {
    updateState { state ->
      val ingredients = state.input.ingredients.toMutableList()
      state.copy(
        input = state.input.copy(
          ingredients = ingredients.map { item ->
            if (item.id == ingredientId && item is IngredientsItem.Ingredient) {
              item.copy(amount = amount)
            } else {
              item
            }
          }
        ))
    }
  }

  private suspend fun setIngredientUnit(
    ingredientId: String,
    unit: MeasureUnit?,
  ) {
    updateState { state ->
      val ingredients = state.input.ingredients.toMutableList()
      state.copy(
        input = state.input.copy(
          ingredients = ingredients.map { item ->
            if (item.id == ingredientId && item is IngredientsItem.Ingredient) {
              item.copy(measureUnit = unit)
            } else {
              item
            }
          }
        ))
    }
  }

  private suspend fun moveIngredientItem(
    from: Int,
    to: Int,
  ) {
    updateState { state ->
      if (state.input.ingredients.size <= max(from, to)) return@updateState state
      state.copy(
        input = state.input.copy(
          ingredients = state.input.ingredients.toMutableList().apply { add(to, removeAt(from)) }
        )
      )
    }
  }

  private suspend fun deleteIngredientItem(
    ingredientId: String,
  ) {
    sendEffect(RecipeInputScreenEffect.OnBottomSheetClosed)
    updateState { state ->
      state.copy(input = state.input.copy(
        ingredients = state.input.ingredients.filter { ingredient -> ingredient.id != ingredientId }
      ))
    }
  }

  companion object {
    private const val MAX_NAME_LENGTH = 100
  }
}
