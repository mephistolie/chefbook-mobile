package com.mysty.chefbook.features.recipe.input.ui.viewmodel

import com.mysty.chefbook.api.common.communication.safeData
import com.mysty.chefbook.api.files.domain.usecases.ICompressImageUseCase
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputCookingScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

internal class RecipeInputScreenViewModelCookingDelegate(
  private val updateState: suspend (block: suspend (RecipeInputScreenState) -> RecipeInputScreenState) -> Unit,
  private val sendEffect: suspend (RecipeInputScreenEffect) -> Unit,

  private val compressImageUseCase: ICompressImageUseCase,
) {

  suspend fun reduceIntent(intent: RecipeInputCookingScreenIntent) {
    when (intent) {
      is RecipeInputCookingScreenIntent.AddStep ->
        addCookingItem(CookingItem.Step(UUID.randomUUID().toString(), Strings.EMPTY))
      is RecipeInputCookingScreenIntent.AddCookingSection ->
        addCookingItem(CookingItem.Section(UUID.randomUUID().toString(), Strings.EMPTY))
      is RecipeInputCookingScreenIntent.SetCookingItemValue ->
        setCookingItemValue(itemIndex = intent.index, value = intent.value)
      is RecipeInputCookingScreenIntent.AddStepPicture ->
        addStepPicture(stepIndex = intent.stepIndex, uri = intent.uri)
      is RecipeInputCookingScreenIntent.DeleteStepPicture ->
        removeStepPicture(stepIndex = intent.stepIndex, pictureIndex = intent.pictureIndex)
      is RecipeInputCookingScreenIntent.MoveStepItem -> moveCookingItem(intent.from, intent.to)
      is RecipeInputCookingScreenIntent.DeleteStepItem -> deleteCookingItem(intent.index)
    }
  }

  private suspend fun addCookingItem(
    item: CookingItem,
  ) {
    updateState { state ->
      val cooking = state.input.cooking.toMutableList()
      cooking.add(item)
      state.copy(input = state.input.copy(cooking = cooking))
    }
  }

  private suspend fun setCookingItemValue(
    itemIndex: Int,
    value: String,
  ) {
    updateState { state ->
      val cooking = state.input.cooking.toMutableList()
      state.copy(
        input = state.input.copy(cooking = cooking.mapIndexed { index, item ->
          if (index != itemIndex) {
            item
          } else {
            when (item) {
              is CookingItem.Section -> item.copy(
                name = value.substring(0, min(value.length, MAX_NAME_LENGTH))
              )
              is CookingItem.Step -> item.copy(
                description = value.substring(0, min(value.length, MAX_STEP_LENGTH))
              )
              else -> item
            }
          }
        })
      )
    }
  }

  private suspend fun addStepPicture(
    stepIndex: Int, uri: String
  ) {
    val compressedPicturePath = compressImageUseCase(path = uri).safeData() ?: uri
    updateState { state ->
      val input = state.input
      val step = (input.cooking.getOrNull(stepIndex) as? CookingItem.Step) ?: return@updateState state
      val pictures = (step.pictures?.toMutableList() ?: mutableListOf())
      pictures.add(compressedPicturePath)
      val updatedStep = step.copy(pictures = pictures)
      state.copy(input = input.copy(
        cooking = input.cooking.mapIndexed { index, item ->
          if (index != stepIndex) item else updatedStep
        }
      ))
    }
  }

  private suspend fun removeStepPicture(
    stepIndex: Int,
    pictureIndex: Int,
  ) {
    updateState { state ->
      val input = state.input
      state.copy(
        input = input.copy(
          cooking = input.cooking.mapIndexed { index, item ->
            if (index != stepIndex || item !is CookingItem.Step) item
            else {
              val pictures = item.pictures?.filterIndexed { i, _ -> i != pictureIndex }
              item.copy(pictures = pictures?.ifEmpty { null })
            }
          }
        )
      )
    }
  }

  private suspend fun moveCookingItem(
    from: Int,
    to: Int,
  ) {
    updateState { state ->
      if (state.input.cooking.size <= max(from, to)) return@updateState state
      state.copy(
        input = state.input.copy(
          cooking = state.input.cooking.toMutableList().apply { add(to, removeAt(from)) }
        )
      )
    }
  }

  private suspend fun deleteCookingItem(
    itemIndex: Int,
  ) {
    sendEffect(RecipeInputScreenEffect.OnBottomSheetClosed)
    updateState { state ->
      state.copy(input = state.input.copy(
        ingredients = state.input.ingredients.filterIndexed { index, _ -> index != itemIndex }
      ))
    }
  }

  companion object {
    private const val MAX_NAME_LENGTH = 100
    private const val MAX_STEP_LENGTH = 6000
  }
}
