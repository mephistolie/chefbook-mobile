package com.mysty.chefbook.features.recipe.control.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.recipe.domain.entities.toRecipeInfo
import com.mysty.chefbook.api.recipe.domain.usecases.IDeleteRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeFavouriteStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeSaveStatusUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipe.control.R
import com.mysty.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenEffect
import com.mysty.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent
import com.mysty.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IRecipeControlScreenViewModel = IMviViewModel<RecipeControlScreenState, RecipeControlScreenIntent, RecipeControlScreenEffect>

internal class RecipeControlScreenViewModel(
  private val recipeId: String,

  private val observeRecipeUseCase: IObserveRecipeUseCase,
  private val setRecipeFavouriteStatusUseCase: ISetRecipeFavouriteStatusUseCase,
  private val setRecipeSaveStatusUseCase: ISetRecipeSaveStatusUseCase,
  private val deleteRecipeUseCase: IDeleteRecipeUseCase,
) : MviViewModel<RecipeControlScreenState, RecipeControlScreenIntent, RecipeControlScreenEffect>() {

  override val _state: MutableStateFlow<RecipeControlScreenState> = MutableStateFlow(RecipeControlScreenState())

  init {
    viewModelScope.launch {
      observeRecipe(recipeId)
    }
  }

  private suspend fun observeRecipe(recipeId: String) {
    observeRecipeUseCase.invoke(
      recipeId = recipeId,
      onError = { _effect.emit(RecipeControlScreenEffect.Close) }
    )
      .collect { recipe ->
        if (recipe != null) _state.emit(RecipeControlScreenState(recipe.toRecipeInfo()))
      }
  }

  override suspend fun reduceIntent(intent: RecipeControlScreenIntent) {
    when (intent) {
      is RecipeControlScreenIntent.ChangeSavedStatus -> changeRecipeSaveStatusUseCase()
      is RecipeControlScreenIntent.ChangeFavouriteStatus -> changeRecipeFavouriteStatusUseCase()
      is RecipeControlScreenIntent.ChangeCategories -> _effect.emit(RecipeControlScreenEffect.OpenCategoriesSelectionPage)
      is RecipeControlScreenIntent.EditRecipe -> _effect.emit(RecipeControlScreenEffect.EditRecipe(recipeId = recipeId))
      is RecipeControlScreenIntent.DeleteRecipe -> deleteRecipe()
      is RecipeControlScreenIntent.Close -> _effect.emit(RecipeControlScreenEffect.Close)
      is RecipeControlScreenIntent.OpenRemoveFromRecipeBookDialog -> {
        _effect.emit(RecipeControlScreenEffect.OpenRemoveFromRecipeBookConfirmDialog)
      }
      is RecipeControlScreenIntent.OpenDeleteDialog -> {
        _effect.emit(RecipeControlScreenEffect.OpenDeleteRecipeConfirmDialog)
      }
    }
  }

  private suspend fun changeRecipeSaveStatusUseCase() {
    setRecipeSaveStatusUseCase(recipeId, !(state.value.recipe?.isSaved ?: false))
  }

  private suspend fun changeRecipeFavouriteStatusUseCase() {
    setRecipeFavouriteStatusUseCase(recipeId, !(state.value.recipe?.isFavourite ?: false))
  }

  private suspend fun deleteRecipe() {
    deleteRecipeUseCase.invoke(recipeId)
      .collect { result ->
        when (result) {
          is Successful -> {
            _effect.emit(RecipeControlScreenEffect.ShowToast(R.string.common_recipe_control_screen_recipe_deleted))
            _effect.emit(RecipeControlScreenEffect.Close)
          }
          is Failure -> _effect.emit(RecipeControlScreenEffect.ShowToast(R.string.common_general_cant_perform_operation))
          else -> Unit
        }
      }
  }

}
