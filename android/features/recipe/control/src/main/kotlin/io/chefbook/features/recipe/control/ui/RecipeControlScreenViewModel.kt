package io.chefbook.features.recipe.control.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipe.control.R
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenEffect
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.DeleteRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipeUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeFavouriteStatusUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeSavedStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import io.chefbook.core.android.R as coreR

internal typealias IRecipeControlScreenViewModel = MviViewModel<RecipeControlScreenState, RecipeControlScreenIntent, RecipeControlScreenEffect>

internal class RecipeControlScreenViewModel(
  private val recipeId: String,

  private val observeRecipeUseCase: ObserveRecipeUseCase,
  private val setRecipeFavouriteStatusUseCase: SetRecipeFavouriteStatusUseCase,
  private val setRecipeSavedStatusUseCase: SetRecipeSavedStatusUseCase,
  private val deleteRecipeUseCase: DeleteRecipeUseCase,
) :
  BaseMviViewModel<RecipeControlScreenState, RecipeControlScreenIntent, RecipeControlScreenEffect>() {

  override val _state: MutableStateFlow<RecipeControlScreenState> =
    MutableStateFlow(RecipeControlScreenState())

  init {
    viewModelScope.launch {
      observeRecipe(recipeId)
    }
  }

  private suspend fun observeRecipe(recipeId: String) {
    observeRecipeUseCase.invoke(recipeId = recipeId)
      .catch { _effect.emit(RecipeControlScreenEffect.Close) }
      .collect { recipe ->
        (recipe as? DecryptedRecipe)?.let { _state.emit(RecipeControlScreenState(recipe.info)) }
      }
  }

  override suspend fun reduceIntent(intent: RecipeControlScreenIntent) {
    when (intent) {
      is RecipeControlScreenIntent.ChangeSavedStatus -> changeRecipeSaveStatusUseCase()
      is RecipeControlScreenIntent.ChangeFavouriteStatus -> changeRecipeFavouriteStatusUseCase()
      is RecipeControlScreenIntent.ChangeCategories -> _effect.emit(RecipeControlScreenEffect.OpenCategoriesSelectionPage)
      is RecipeControlScreenIntent.EditRecipe -> _effect.emit(
        RecipeControlScreenEffect.EditRecipe(
          recipeId = recipeId
        )
      )

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
    setRecipeSavedStatusUseCase(recipeId, !(state.value.recipe?.isSaved ?: false))
  }

  private suspend fun changeRecipeFavouriteStatusUseCase() {
    setRecipeFavouriteStatusUseCase(recipeId, !(state.value.recipe?.isFavourite ?: false))
  }

  private suspend fun deleteRecipe() {
    deleteRecipeUseCase.invoke(recipeId)
      .onSuccess {
        _effect.emit(RecipeControlScreenEffect.ShowToast(R.string.common_recipe_control_screen_recipe_deleted))
        _effect.emit(RecipeControlScreenEffect.Close)
      }
      .onFailure {
        _effect.emit(RecipeControlScreenEffect.ShowToast(coreR.string.common_general_cant_perform_operation))
      }
  }
}
