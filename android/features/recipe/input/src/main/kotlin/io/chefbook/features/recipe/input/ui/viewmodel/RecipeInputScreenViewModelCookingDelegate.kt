package io.chefbook.features.recipe.input.ui.viewmodel

import io.chefbook.features.recipe.input.ui.mvi.RecipeInputCookingScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput.CookingItem
import kotlin.math.max
import kotlin.math.min

internal class RecipeInputScreenViewModelCookingDelegate(
  private val updateState: (block: (RecipeInputScreenState) -> RecipeInputScreenState) -> Unit,
  private val sendEffect: suspend (RecipeInputScreenEffect) -> Unit,
  private val deletePicture: (String) -> Unit,
) {

  suspend fun reduceIntent(intent: RecipeInputCookingScreenIntent) {
    when (intent) {
      is RecipeInputCookingScreenIntent.AddStep ->
        addCookingItem(CookingItem.Step(generateUUID(), ""))
      is RecipeInputCookingScreenIntent.AddCookingSection ->
        addCookingItem(CookingItem.Section(generateUUID(), ""))
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
                name = value.substring(0, min(value.length, MAX_SECTION_LENGTH))
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
    updateState { state ->
      val input = state.input
      val step = (input.cooking.getOrNull(stepIndex) as? CookingItem.Step) ?: return@updateState state
      val updatedStep = step.copy(pictures = step.pictures.plus(RecipeInput.Picture.Pending(uri)))
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
      val picture = (input.cooking[stepIndex] as? CookingItem.Step)?.pictures?.getOrNull(pictureIndex)
      state.copy(
        input = input.copy(
          cooking = input.cooking.mapIndexed { index, item ->
            if (index != stepIndex || item !is CookingItem.Step) item
            else {
              val pictures = item.pictures.filterIndexed { i, _ -> i != pictureIndex }
              item.copy(pictures = pictures)
            }
          }
        ).also {
          if (picture is RecipeInput.Picture.Pending) deletePicture(picture.source)
        }
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
      val pictures = (state.input.cooking[itemIndex] as? CookingItem.Step)?.pictures
      state.copy(input = state.input.copy(
        cooking = state.input.cooking.filterIndexed { index, _ -> index != itemIndex }
      )).also {
        pictures?.forEach {
          if (it is RecipeInput.Picture.Pending) deletePicture(it.source)
        }
      }
    }
  }

  companion object {
    private const val MAX_SECTION_LENGTH = 80
    private const val MAX_STEP_LENGTH = 2500
  }
}
