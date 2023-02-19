package com.mysty.chefbook.features.recipe.input.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.encryption.domain.usecases.IGetEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.files.domain.usecases.ICompressImageUseCase
import com.mysty.chefbook.api.recipe.domain.entities.toRecipeInput
import com.mysty.chefbook.api.recipe.domain.usecases.ICreateRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IGetRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IUpdateRecipeUseCase
import com.mysty.chefbook.api.settings.domain.usecases.IGetDefaultRecipeLanguageUseCase
import com.mysty.chefbook.api.settings.domain.usecases.ISetDefaultRecipeLanguageUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val BOTTOM_SHEET_HIDE_ANIMATION_DURATION = 200L

internal typealias IRecipeInputScreenViewModel = IMviViewModel<RecipeInputScreenState, RecipeInputScreenIntent, RecipeInputScreenEffect>

class RecipeInputScreenViewModel(
  private val recipeId: String?,

  private val getRecipeUseCase: IGetRecipeUseCase,
  private val createRecipeUseCase: ICreateRecipeUseCase,
  private val updateRecipeUseCase: IUpdateRecipeUseCase,
  private val observeEncryptedVaultStateUseCase: IObserveEncryptedVaultStateUseCase,
  private val getDefaultRecipeLanguageUseCase: IGetDefaultRecipeLanguageUseCase,
  setDefaultRecipeLanguageUseCase: ISetDefaultRecipeLanguageUseCase,
  getEncryptedVaultStateUseCase: IGetEncryptedVaultStateUseCase,
  compressImageUseCase: ICompressImageUseCase,
) : MviViewModel<RecipeInputScreenState, RecipeInputScreenIntent, RecipeInputScreenEffect>() {

  override val _state: MutableStateFlow<RecipeInputScreenState> =
    MutableStateFlow(RecipeInputScreenState())

  private var encryptionOnVaultUnlockRequested = false

  private val detailsDelegate = RecipeInputScreenViewModelDetailsDelegate(
    updateState = this::updateStateSafely,
    sendEffect = _effect::emit,
    requestEncryptionOnVaultUnlock = { encryptionOnVaultUnlockRequested = true },
    setDefaultRecipeLanguageUseCase = setDefaultRecipeLanguageUseCase,
    getEncryptedVaultStateUseCase = getEncryptedVaultStateUseCase,
    compressImageUseCase = compressImageUseCase,
  )

  private val ingredientsDelegate = RecipeInputScreenViewModelIngredientsDelegate(
    updateState = this::updateStateSafely,
    sendEffect = _effect::emit,
  )

  private val cookingDelegate = RecipeInputScreenViewModelCookingDelegate(
    updateState = this::updateStateSafely,
    sendEffect = _effect::emit,
    compressImageUseCase = compressImageUseCase,
  )

  init {
    viewModelScope.launch {
      if (recipeId != null) {
        loadRecipe(recipeId = recipeId)
      } else {
        updateStateSafely { state -> state.copy(input = state.input.copy(language = getDefaultRecipeLanguageUseCase())) }
      }
      observeEncryptedVaultUnlock()
    }
  }

  private suspend fun loadRecipe(recipeId: String) {
    getRecipeUseCase(recipeId)
      .collect { result ->
        when (result) {
          is Loading -> _state.emit(state.value.copy(isLoading = true))
          is Successful -> _state.emit(
            RecipeInputScreenState(recipeId = recipeId, input = result.data.toRecipeInput())
          )
          is Failure -> closeInput()
        }
      }
  }

  private suspend fun observeEncryptedVaultUnlock() {
    observeEncryptedVaultStateUseCase()
      .collect { vaultState ->
        if (vaultState is EncryptedVaultState.Unlocked && encryptionOnVaultUnlockRequested) {
          val currentState = state.value
          _state.emit(state.value.copy(input = currentState.input.copy(isEncrypted = true)))
          delay(BOTTOM_SHEET_HIDE_ANIMATION_DURATION)
          _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
          encryptionOnVaultUnlockRequested = false
        }
      }
  }

  override suspend fun reduceIntent(intent: RecipeInputScreenIntent) {
    when (intent) {
      is RecipeInputScreenIntent.Details -> detailsDelegate.reduceIntent(intent.data)
      is RecipeInputScreenIntent.Ingredients -> ingredientsDelegate.reduceIntent(intent.data)
      is RecipeInputScreenIntent.Cooking -> cookingDelegate.reduceIntent(intent.data)

      is RecipeInputScreenIntent.Save -> saveRecipe()
      is RecipeInputScreenIntent.CloseBottomSheet -> _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
      is RecipeInputScreenIntent.Back -> _effect.emit(RecipeInputScreenEffect.OnBack)
      is RecipeInputScreenIntent.Continue -> _effect.emit(RecipeInputScreenEffect.OnContinue)
      is RecipeInputScreenIntent.Close -> closeInput(intent.openRecipeScreen)
    }
  }

  private suspend fun saveRecipe() {
    val currentState = state.value

    if (currentState.recipeId == null) {
      createRecipeUseCase(currentState.input)
        .collect { result ->
          _state.emit(
            when (result) {
              is Loading -> currentState.copy(isLoading = true)
              is Successful -> {
                _effect.emit(RecipeInputScreenEffect.OnSaved)
                currentState.copy(recipeId = result.data.id)
              }
              is Failure -> currentState
            }
          )
        }
    } else {
      updateRecipeUseCase(currentState.recipeId, currentState.input)
        .collect { result ->
          when (result) {
            is Loading -> _state.emit(currentState.copy(isLoading = true))
            is Successful -> closeInput(openRecipeScreen = true)
            is Failure -> _state.emit(currentState)
          }
        }
    }
  }

  private suspend fun closeInput(openRecipeScreen: Boolean = false) {
    _effect.emit(
      RecipeInputScreenEffect.OnClose(recipeId = if (openRecipeScreen) state.value.recipeId else null)
    )
  }

}
