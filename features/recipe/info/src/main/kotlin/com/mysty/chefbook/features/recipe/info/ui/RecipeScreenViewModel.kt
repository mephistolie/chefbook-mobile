package com.mysty.chefbook.features.recipe.info.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.api.recipe.domain.usecases.IGetRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeLikeStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeSaveStatusUseCase
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IAddToShoppingListUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.features.recipe.info.R
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenEffect
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.features.recipe.info.ui.presentation.RecipeScreenBottomSheetType
import kotlin.math.ceil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IRecipeScreenViewModel = IMviViewModel<RecipeScreenState, RecipeScreenIntent, RecipeScreenEffect>

internal class RecipeScreenViewModel(
  private val recipeId: String,

  private val observeRecipeUseCase: IObserveRecipeUseCase,
  private val getRecipeUseCase: IGetRecipeUseCase,
  private val setRecipeSaveStatusUseCase: ISetRecipeSaveStatusUseCase,
  private val setRecipeLikeStatusUseCase: ISetRecipeLikeStatusUseCase,
  private val addToShoppingListUseCase: IAddToShoppingListUseCase,
) : MviViewModel<RecipeScreenState, RecipeScreenIntent, RecipeScreenEffect>() {

  override val _state: MutableStateFlow<RecipeScreenState> =
    MutableStateFlow(RecipeScreenState.Loading)

  init {
    viewModelScope.launch {
      observeRecipe(recipeId)
    }
  }

  private suspend fun observeRecipe(recipeId: String) {
    _state.emit(RecipeScreenState.Loading)
    observeRecipeUseCase.invoke(
      recipeId = recipeId,
      onError = { error -> _state.emit(RecipeScreenState.Error(recipeId, error)) }
    )
      .collect { recipe ->
        if (recipe != null) _state.emit(RecipeScreenState.Success(recipe))
        else _effect.emit(RecipeScreenEffect.Close)
      }
  }

  override suspend fun reduceIntent(intent: RecipeScreenIntent) {
    when (intent) {
      is RecipeScreenIntent.ReloadRecipe -> getRecipeUseCase(recipeId)
      is RecipeScreenIntent.AddToRecipeBook -> addRecipeToRecipeBook()
      is RecipeScreenIntent.OpenRecipeMenu -> openRecipeMenu()
      is RecipeScreenIntent.OpenRecipeDetails -> TODO()
      is RecipeScreenIntent.ChangeLikeStatus -> changeRecipeLikeStatus()
      is RecipeScreenIntent.ChangeIngredientSelectedStatus -> changeIngredientSelectedStatus(intent.ingredientId)
      is RecipeScreenIntent.ChangeServings -> changeServingsMultiplier(intent.offset)
      is RecipeScreenIntent.AddSelectedIngredientsToShoppingList -> addSelectedIngredientsToShoppingList()
      is RecipeScreenIntent.Close -> _effect.emit(RecipeScreenEffect.Close)

      is RecipeScreenIntent.OpenShareDialog -> _effect.emit(
        RecipeScreenEffect.OpenShareDialog(
          recipeId = recipeId
        )
      )
      is RecipeScreenIntent.OpenPicturesViewer -> openPicturesViewer(selectedPicture = intent.selectedPicture)
    }
  }

  private suspend fun addRecipeToRecipeBook() {
    (state.value as? RecipeScreenState.Success)?.let { state ->
      setRecipeSaveStatusUseCase(state.recipe.id, true)
    }
  }

  private suspend fun openRecipeMenu() {
    updateRecipeStateSafely { state ->
      state.copy(bottomSheetType = RecipeScreenBottomSheetType.MENU)
    }
    _effect.emit(RecipeScreenEffect.OpenBottomSheet)
  }

  private suspend fun changeRecipeLikeStatus() {
    (state.value as? RecipeScreenState.Success)?.let { state ->

      val recipe = state.recipe
      _state.emit(
        state.copy(
          recipe = recipe.copy(
            isLiked = !recipe.isLiked,
            likes = recipe.likes?.let { if (recipe.isLiked) it - 1 else it + 1 },
          )
        )
      )

      setRecipeLikeStatusUseCase(recipe.id, !recipe.isLiked)
    }
  }

  private suspend fun openPicturesViewer(
    selectedPicture: String = Strings.EMPTY,
  ) {
    (state.value as? RecipeScreenState.Success)?.let { state ->
      val pictures = mutableListOf<String>()
      state.recipe.preview?.let { pictures.add(it) }
      state.recipe.cooking.onEach { item ->
        if (item is CookingItem.Step) pictures.addAll(item.pictures ?: emptyList())
      }
      val startIndex = pictures.indexOfFirst { it == selectedPicture }

      _effect.emit(
        RecipeScreenEffect.OpenPicturesViewer(
          pictures = pictures,
          startIndex = startIndex
        )
      )
    }
  }


  private suspend fun changeServingsMultiplier(
    offset: Int,
  ) {
    updateRecipeStateSafely { state ->
      var multiplier = state.servingsMultiplier + offset
      when {
        multiplier < 1 -> multiplier = 1
        multiplier > 99 -> multiplier = 99
      }
      state.copy(servingsMultiplier = multiplier)
    }
  }

  private suspend fun changeIngredientSelectedStatus(
    ingredientId: String,
  ) {
    updateRecipeStateSafely { state ->
      val selectedIngredients = state.selectedIngredients.toMutableSet()
      if (selectedIngredients.contains(ingredientId)) {
        selectedIngredients.remove(ingredientId)
      } else {
        selectedIngredients.add(ingredientId)
      }
      state.copy(selectedIngredients = selectedIngredients.toSet())
    }
  }

  private suspend fun addSelectedIngredientsToShoppingList() {
    updateRecipeStateSafely { state ->
      val servings = state.recipe.servings
      val multiplier = state.servingsMultiplier.toFloat() / (servings ?: 1)
      val purchases = state.recipe.ingredients
        .filterIsInstance<IngredientItem.Ingredient>()
        .filter { it.id in state.selectedIngredients }
        .map { ingredient ->
          Purchase(
            id = ingredient.id,
            name = ingredient.name,
            amount = ingredient.amount?.let { amount -> ceil(amount * multiplier).toInt() },
            unit = ingredient.unit,
            multiplier = if (ingredient.amount == null && servings != null) ceil(multiplier * servings).toInt() else multiplier.toInt(),
            recipeId = if (state.recipe.encryptionState is EncryptionState.Standard) state.recipe.id else null,
            recipeName = if (state.recipe.encryptionState is EncryptionState.Standard) state.recipe.name else null,
          )
        }

      addToShoppingListUseCase(purchases)
      _effect.emit(RecipeScreenEffect.ShowToast(R.string.common_recipe_screen_ingredients_added_to_shopping_list))
      state.copy(selectedIngredients = emptySet())
    }
  }

  private suspend fun updateRecipeStateSafely(
    block: suspend (RecipeScreenState.Success) -> RecipeScreenState
  ) {
    updateStateSafely { state ->
      (state as? RecipeScreenState.Success)?.let { block(it) } ?: state
    }
  }
}
