package com.mysty.chefbook.features.recipe.input.ui.viewmodel

import com.mysty.chefbook.api.common.communication.safeData
import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.encryption.domain.usecases.IGetEncryptedVaultStateUseCase
import com.mysty.chefbook.api.files.domain.usecases.ICompressImageUseCase
import com.mysty.chefbook.api.recipe.domain.entities.macronutrients.MacronutrientsInfo
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import com.mysty.chefbook.api.settings.domain.usecases.ISetDefaultRecipeLanguageUseCase
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState

internal class RecipeInputScreenViewModelDetailsDelegate(
  private val updateState: suspend (block: suspend (RecipeInputScreenState) -> RecipeInputScreenState) -> Unit,
  private val sendEffect: suspend (RecipeInputScreenEffect) -> Unit,
  private val requestEncryptionOnVaultUnlock: () -> Unit,

  private val setDefaultRecipeLanguageUseCase: ISetDefaultRecipeLanguageUseCase,
  private val getEncryptedVaultStateUseCase: IGetEncryptedVaultStateUseCase,
  private val compressImageUseCase: ICompressImageUseCase,
) {
  
  suspend fun reduceIntent(intent: RecipeInputDetailsScreenIntent) {
    when (intent) {
      is RecipeInputDetailsScreenIntent.SetName -> setName(intent.name)
      is RecipeInputDetailsScreenIntent.OpenVisibilityPicker ->
        sendEffect(RecipeInputScreenEffect.Details.OnVisibilityPickerOpen)
      is RecipeInputDetailsScreenIntent.SetVisibility -> setVisibility(intent.visibility)
      is RecipeInputDetailsScreenIntent.OpenEncryptedStatePicker ->
        sendEffect(RecipeInputScreenEffect.Details.OnEncryptionStatePickerOpen)
      is RecipeInputDetailsScreenIntent.SetEncryptedState -> setEncryptedState(intent.isEncrypted)
      is RecipeInputDetailsScreenIntent.OpenLanguagePicker ->
        sendEffect(RecipeInputScreenEffect.Details.OnLanguagePickerOpen)
      is RecipeInputDetailsScreenIntent.SetLanguage -> setLanguage(intent.language)
      is RecipeInputDetailsScreenIntent.SetDescription -> setDescription(intent.description)
      is RecipeInputDetailsScreenIntent.SetPreview -> setCompressedPreview(intent.uri)
      is RecipeInputDetailsScreenIntent.RemovePreview -> removePreview()
      is RecipeInputDetailsScreenIntent.SetServings -> setServings(intent.servings)
      is RecipeInputDetailsScreenIntent.SetTime -> setTime(intent.h, intent.min)
      is RecipeInputDetailsScreenIntent.OpenCaloriesDialog ->
        sendEffect(RecipeInputScreenEffect.Details.OnCaloriesDialogOpen)
      is RecipeInputDetailsScreenIntent.SetCalories -> setCalories(intent.calories)
      is RecipeInputDetailsScreenIntent.SetProtein -> setProtein(intent.protein)
      is RecipeInputDetailsScreenIntent.SetFats -> setFats(intent.fats)
      is RecipeInputDetailsScreenIntent.SetCarbohydrates -> setCarbs(intent.carbs)
    }
  }

  private suspend fun setName(name: String) {
    val croppedName =
      if (name.length > MAX_NAME_LENGTH) name.substring(0, MAX_NAME_LENGTH) else name
    updateState { state -> state.copy(input = state.input.copy(name = croppedName)) }
  }

  private suspend fun setVisibility(visibility: Visibility) {
    updateState { state ->
      val isEncrypted = if (visibility == Visibility.PUBLIC) false else state.input.isEncrypted
      state.copy(input = state.input.copy(visibility = visibility, isEncrypted = isEncrypted))
    }
    sendEffect(RecipeInputScreenEffect.OnBottomSheetClosed)
  }

  private suspend fun setEncryptedState(isEncrypted: Boolean) {
    if (getEncryptedVaultStateUseCase() !is EncryptedVaultState.Unlocked) {
      requestEncryptionOnVaultUnlock()
      sendEffect(RecipeInputScreenEffect.Details.OnEncryptedVaultMenuOpen)
      return
    }
    updateState { state ->
      val visibility =
        if (isEncrypted && state.input.visibility == Visibility.PUBLIC) Visibility.SHARED else state.input.visibility
      state.copy(input = state.input.copy(visibility = visibility, isEncrypted = isEncrypted))
    }
    sendEffect(RecipeInputScreenEffect.OnBottomSheetClosed)
  }

  private suspend fun setLanguage(language: Language) {
    updateState { state -> state.copy(input = state.input.copy(language = language)) }
    setDefaultRecipeLanguageUseCase.invoke(language)
    sendEffect(RecipeInputScreenEffect.OnBottomSheetClosed)
  }

  private suspend fun setDescription(description: String) {
    val croppedDescription =
      when {
        (description.length > MAX_DESCRIPTION_LENGTH) -> {
          description.substring(0, MAX_DESCRIPTION_LENGTH)
        }
        (description.isNotEmpty()) -> description
        else -> null
      }
    updateState { state -> state.copy(input = state.input.copy(description = croppedDescription)) }
  }

  private suspend fun setCompressedPreview(uri: String) {
    val compressedPreviewPath = compressImageUseCase(path = uri).safeData() ?: uri
    updateState { state -> state.copy(input = state.input.copy(preview = compressedPreviewPath)) }
  }

  private suspend fun removePreview() {
    updateState { state -> state.copy(input = state.input.copy(preview = null)) }
  }

  private suspend fun setServings(count: Int?) {
    val servings = when {
      count == null -> null
      count > 99 -> 99
      count < 1 -> 1
      else -> count
    }
    updateState { state -> state.copy(input = state.input.copy(servings = servings)) }
  }

  private suspend fun setTime(h: Int, min: Int) {
    val time = h * 60 + min
    updateState { state -> state.copy(input = state.input.copy(time = if (time > 0) time else null)) }
  }

  private suspend fun setCalories(value: Int?) {
    val calories = if (value != null && value > 0) value else null
    updateState { state -> state.copy(input = state.input.copy(calories = calories)) }
  }

  private suspend fun setProtein(value: Int?) {
    val protein = if (value != null && value > 0) value else null
    updateState { state ->
      val macronutrients = state.input.macronutrients?.copy(protein = protein)
        ?: MacronutrientsInfo(protein = protein)
      state.copy(input = state.input.copy(macronutrients = macronutrients.filtered()))
    }
  }

  private suspend fun setFats(value: Int?) {
    updateState { state ->
      val fats = if (value != null && value > 0) value else null
      val macronutrients = state.input.macronutrients?.copy(fats = fats)
        ?: MacronutrientsInfo(fats = fats)
      state.copy(input = state.input.copy(macronutrients = macronutrients.filtered()))
    }
  }

  private suspend fun setCarbs(value: Int?) {
    updateState { state ->
      val carbs = if (value != null && value > 0) value else null
      val macronutrients = state.input.macronutrients?.copy(carbohydrates = carbs)
        ?: MacronutrientsInfo(carbohydrates = carbs)
      state.copy(input = state.input.copy(macronutrients = macronutrients.filtered()))
    }
  }

  private fun MacronutrientsInfo.filtered() =
    if (protein != null || fats != null || carbohydrates != null) this else null

  companion object {
    private const val MAX_NAME_LENGTH = 100
    private const val MAX_DESCRIPTION_LENGTH = 1500
  }
}
