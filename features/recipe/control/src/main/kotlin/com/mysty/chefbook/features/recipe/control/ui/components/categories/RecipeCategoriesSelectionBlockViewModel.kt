package com.mysty.chefbook.features.recipe.control.ui.components.categories

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoriesUseCase
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeCategoriesUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipe.control.R
import com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockEffect
import com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockIntent
import com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IRecipeCategoriesSelectionBlockViewModel = IMviViewModel<RecipeCategoriesSelectionBlockState, RecipeCategoriesSelectionBlockIntent, RecipeCategoriesSelectionBlockEffect>

internal class RecipeCategoriesSelectionBlockViewModel(
  private val recipe: RecipeInfo,

  private val getCategoriesUseCase: IGetCategoriesUseCase,
  private val setRecipeCategoriesUseCase: ISetRecipeCategoriesUseCase,
) :
  MviViewModel<RecipeCategoriesSelectionBlockState, RecipeCategoriesSelectionBlockIntent, RecipeCategoriesSelectionBlockEffect>() {

  override val _state: MutableStateFlow<RecipeCategoriesSelectionBlockState> =
    MutableStateFlow(RecipeCategoriesSelectionBlockState(recipe = recipe, isLoading = true))

  init {
    viewModelScope.launch {
      updateStateSafely { state ->
        state.copy(categories = getCategoriesUseCase(), isLoading = false)
      }
    }
  }

  override suspend fun reduceIntent(intent: RecipeCategoriesSelectionBlockIntent) {
    when (intent) {
      is RecipeCategoriesSelectionBlockIntent.Cancel -> {
        updateStateSafely { state -> state.copy(selectedCategories = recipe.categories.map { it.id }) }
        _effect.emit(RecipeCategoriesSelectionBlockEffect.Close)
      }
      is RecipeCategoriesSelectionBlockIntent.ChangeSelectStatus -> {
        updateStateSafely { state ->
          val category = intent.category
          val selectedCategories = state.selectedCategories.toMutableList()
          if (category in selectedCategories) selectedCategories.remove(category) else selectedCategories.add(
            category
          )
          state.copy(selectedCategories = selectedCategories)
        }
      }
      is RecipeCategoriesSelectionBlockIntent.ConfirmSelection -> {
        withSafeState { state ->
          setRecipeCategoriesUseCase(
            recipeId = recipe.id, categories = state.selectedCategories
          ).collect { result ->
              _state.emit(state.copy(isLoading = result is Loading))
              when (result) {
                is Successful -> {
                  _effect.emit(RecipeCategoriesSelectionBlockEffect.ShowToast(R.string.common_recipe_categories_selection_block_categories_chosen))
                  _effect.emit(RecipeCategoriesSelectionBlockEffect.Close)
                }
                is Failure -> {
                  _effect.emit(RecipeCategoriesSelectionBlockEffect.ShowToast(R.string.common_general_unknown_error))
                }
                else -> Unit
              }
            }
        }
      }
    }
  }

}
