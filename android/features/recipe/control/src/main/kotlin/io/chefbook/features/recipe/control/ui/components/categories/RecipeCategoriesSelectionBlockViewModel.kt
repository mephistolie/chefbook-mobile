package io.chefbook.features.recipe.control.ui.components.categories

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipe.control.R
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockEffect
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockIntent
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoriesUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import io.chefbook.core.android.R as coreR

internal typealias IRecipeCategoriesSelectionBlockViewModel = MviViewModel<RecipeCategoriesSelectionBlockState, RecipeCategoriesSelectionBlockIntent, RecipeCategoriesSelectionBlockEffect>

internal class RecipeCategoriesSelectionBlockViewModel(
  private val recipe: DecryptedRecipeInfo,

  private val getCategoriesUseCase: GetCategoriesUseCase,
  private val setRecipeCategoriesUseCase: SetRecipeCategoriesUseCase,
) :
  BaseMviViewModel<RecipeCategoriesSelectionBlockState, RecipeCategoriesSelectionBlockIntent, RecipeCategoriesSelectionBlockEffect>() {

  override val _state: MutableStateFlow<RecipeCategoriesSelectionBlockState> =
    MutableStateFlow(RecipeCategoriesSelectionBlockState(recipe = recipe, isLoading = true))

  init {
    viewModelScope.launch {
      _state.update { state ->
        state.copy(categories = getCategoriesUseCase(), isLoading = false)
      }
    }
  }

  override suspend fun reduceIntent(intent: RecipeCategoriesSelectionBlockIntent) {
    when (intent) {
      is RecipeCategoriesSelectionBlockIntent.Cancel -> {
        _state.update { state -> state.copy(selectedCategories = recipe.categories.map { it.id }) }
        _effect.emit(RecipeCategoriesSelectionBlockEffect.Close)
      }

      is RecipeCategoriesSelectionBlockIntent.ChangeSelectStatus -> {
        _state.update { state ->
          val category = intent.category
          val selectedCategories = state.selectedCategories.toMutableList()
          if (category in selectedCategories) selectedCategories.remove(category) else selectedCategories.add(
            category
          )
          state.copy(selectedCategories = selectedCategories)
        }
      }

      is RecipeCategoriesSelectionBlockIntent.ConfirmSelection -> {
        _state.update { it.copy(isLoading = true) }
        setRecipeCategoriesUseCase(
          recipeId = recipe.id,
          categories = state.value.selectedCategories
        )
          .onSuccess {
            _effect.emit(RecipeCategoriesSelectionBlockEffect.ShowToast(R.string.common_recipe_categories_selection_block_categories_chosen))
            _effect.emit(RecipeCategoriesSelectionBlockEffect.Close)
          }
          .onFailure {
            _state.update { it.copy(isLoading = false) }
            _effect.emit(RecipeCategoriesSelectionBlockEffect.ShowToast(coreR.string.common_general_unknown_error))
          }
      }
    }
  }
}
