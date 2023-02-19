package com.mysty.chefbook.features.recipe.input.ui.viewmodel

import com.mysty.chefbook.api.common.entities.unit.MeasureUnit
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputIngredientsScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

internal class RecipeInputScreenViewModelIngredientsDelegate(
  private val updateState: suspend (block: suspend (RecipeInputScreenState) -> RecipeInputScreenState) -> Unit,
  private val sendEffect: suspend (RecipeInputScreenEffect) -> Unit,
) {

  suspend fun reduceIntent(intent: RecipeInputIngredientsScreenIntent) {
    when (intent) {
      is RecipeInputIngredientsScreenIntent.AddIngredient ->
        addIngredientItem(IngredientItem.Ingredient(UUID.randomUUID().toString(), Strings.EMPTY))
      is RecipeInputIngredientsScreenIntent.AddIngredientSection ->
        addIngredientItem(IngredientItem.Section(UUID.randomUUID().toString(), Strings.EMPTY))
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
    item: IngredientItem,
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
                is IngredientItem.Section -> item.copy(
                  name = name.substring(
                    0,
                    min(name.length, MAX_NAME_LENGTH)
                  )
                )
                is IngredientItem.Ingredient -> item.copy(
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
            if (item.id == ingredientId && item is IngredientItem.Ingredient) {
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
            if (item.id == ingredientId && item is IngredientItem.Ingredient) {
              item.copy(unit = unit)
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
