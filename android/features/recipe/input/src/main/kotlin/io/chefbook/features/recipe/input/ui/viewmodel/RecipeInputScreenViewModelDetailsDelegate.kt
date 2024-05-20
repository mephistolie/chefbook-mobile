package io.chefbook.features.recipe.input.ui.viewmodel

import io.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.GetEncryptedVaultStateUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Macronutrients
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta.Visibility
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.settings.api.external.domain.usecases.SetDefaultRecipeLanguageUseCase

internal class RecipeInputScreenViewModelDetailsDelegate(
  private val updateState: (block: (RecipeInputScreenState) -> RecipeInputScreenState) -> Unit,
  private val sendEffect: suspend (RecipeInputScreenEffect) -> Unit,
  private val requestEncryptionOnVaultUnlock: () -> Unit,

  private val setDefaultRecipeLanguageUseCase: SetDefaultRecipeLanguageUseCase,
  private val getEncryptedVaultStateUseCase: GetEncryptedVaultStateUseCase,
  private val deletePicture: (String) -> Unit,
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
      is RecipeInputDetailsScreenIntent.SetPreview -> setPreview(intent.uri)
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
      val isEncrypted = if (visibility == Visibility.PUBLIC) false else state.input.hasEncryption
      state.copy(input = state.input.copy(visibility = visibility, hasEncryption = isEncrypted))
    }
    sendEffect(RecipeInputScreenEffect.OnBottomSheetClosed)
  }

  private suspend fun setEncryptedState(isEncrypted: Boolean) {
    if (getEncryptedVaultStateUseCase() != EncryptedVaultState.UNLOCKED) {
      requestEncryptionOnVaultUnlock()
      sendEffect(RecipeInputScreenEffect.Details.OnEncryptedVaultMenuOpen)
      return
    }
    updateState { state ->
      val visibility =
        if (isEncrypted && state.input.visibility == Visibility.PUBLIC) Visibility.LINK else state.input.visibility
      state.copy(input = state.input.copy(visibility = visibility, hasEncryption = isEncrypted))
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

  private fun setPreview(uri: String) {
    updateState { state ->
      state.copy(input = state.input.copy(preview = RecipeInput.Picture.Pending(uri)))
    }
  }

  private fun removePreview() {
    updateState { state ->
      val preview = state.input.preview
      state.copy(input = state.input.copy(preview = null)).also {
        if (preview is RecipeInput.Picture.Pending) deletePicture(preview.source)
      }
    }
  }

  private suspend fun setServings(count: Int?) {
    val servings = when {
      count == null -> null
      count > MAX_SERVINGS -> MAX_SERVINGS
      count < 1 -> 1
      else -> count
    }
    updateState { state -> state.copy(input = state.input.copy(servings = servings)) }
  }

  private suspend fun setTime(h: Int, min: Int) {
    var time = h * 60 + min
    if (time > MAX_TIME) time = MAX_TIME
    updateState { state -> state.copy(input = state.input.copy(time = if (time > 0) time else null)) }
  }

  private suspend fun setCalories(value: Int?) {
    var calories = if (value != null && value > 0) value else null
    if (calories != null && calories > MAX_CALORIES) calories = MAX_CALORIES
    updateState { state -> state.copy(input = state.input.copy(calories = calories)) }
  }

  private suspend fun setProtein(value: Int?) {
    var protein = if (value != null && value > 0) value else null
    if (protein != null && protein > MAX_MACRONUTRIENTS) protein = MAX_MACRONUTRIENTS
    updateState { state ->
      val macronutrients = state.input.macronutrients?.copy(protein = protein)
        ?: Macronutrients(protein = protein)
      state.copy(input = state.input.copy(macronutrients = macronutrients.filtered()))
    }
  }

  private suspend fun setFats(value: Int?) {
    var fats = if (value != null && value > 0) value else null
    if (fats != null && fats > MAX_MACRONUTRIENTS) fats = MAX_MACRONUTRIENTS
    updateState { state ->
      val macronutrients = state.input.macronutrients?.copy(fats = fats)
        ?: Macronutrients(fats = fats)
      state.copy(input = state.input.copy(macronutrients = macronutrients.filtered()))
    }
  }

  private suspend fun setCarbs(value: Int?) {
    var carbs = if (value != null && value > 0) value else null
    if (carbs != null && carbs > MAX_MACRONUTRIENTS) carbs = MAX_MACRONUTRIENTS
    updateState { state ->
      val macronutrients = state.input.macronutrients?.copy(carbohydrates = carbs)
        ?: Macronutrients(carbohydrates = carbs)
      state.copy(input = state.input.copy(macronutrients = macronutrients.filtered()))
    }
  }

  private fun Macronutrients.filtered() =
    if (protein != null || fats != null || carbohydrates != null) this else null

  companion object {
    private const val MAX_NAME_LENGTH = 75
    private const val MAX_DESCRIPTION_LENGTH = 100

    private const val MAX_SERVINGS = 1000
    private const val MAX_TIME = 10080
    private const val MAX_CALORIES = 10000
    private const val MAX_MACRONUTRIENTS = 500
  }
}
