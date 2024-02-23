package io.chefbook.features.recipe.info.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipe.info.R
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenEffect
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import io.chefbook.features.recipe.info.ui.state.RecipeScreenBottomSheetType
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.CookingItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.GetRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipeUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeSavedStatusUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.GetSettingsUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.AddToShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal typealias IRecipeScreenViewModel = MviViewModel<RecipeScreenState, RecipeScreenIntent, RecipeScreenEffect>

internal class RecipeScreenViewModel(
  private val recipeId: String,

  private val observeRecipeUseCase: ObserveRecipeUseCase,
  private val getRecipeUseCase: GetRecipeUseCase,
  private val setRecipeSavedStatusUseCase: SetRecipeSavedStatusUseCase,

  private val getShoppingListsUseCase: GetShoppingListsUseCase,
  private val addToShoppingListUseCase: AddToShoppingListUseCase,

  private val getSettingsUseCase: GetSettingsUseCase,
) : BaseMviViewModel<RecipeScreenState, RecipeScreenIntent, RecipeScreenEffect>() {

  override val _state: MutableStateFlow<RecipeScreenState> =
    MutableStateFlow(RecipeScreenState.Loading)

  init {
    viewModelScope.launch {
      observeRecipe(recipeId)
    }
  }

  private suspend fun observeRecipe(recipeId: String) {
    val openExpanded = getSettingsUseCase().openSavedRecipeExpanded
    observeRecipeUseCase(recipeId)
      .catch { error -> _state.emit(RecipeScreenState.Error(recipeId, error)) }
      .collect { recipe ->
        if (recipe is DecryptedRecipe) {
          val lastState = state.value
          _state.update { state ->
            (state as? RecipeScreenState.Success)?.copy(recipe = recipe) ?: RecipeScreenState.Success(recipe)
          }
          if (lastState is RecipeScreenState.Loading && openExpanded) {
            _effect.emit(RecipeScreenEffect.ExpandSheet)
          }
        } else {
          _effect.emit(RecipeScreenEffect.Close)
        }
      }
  }

  override suspend fun reduceIntent(intent: RecipeScreenIntent) {
    when (intent) {
      is RecipeScreenIntent.ReloadRecipe -> getRecipeUseCase(recipeId)
      is RecipeScreenIntent.AddToRecipeBook -> addRecipeToRecipeBook()
      is RecipeScreenIntent.OpenRecipeMenu -> openRecipeMenu()
      is RecipeScreenIntent.OpenRecipeDetails -> TODO()
      is RecipeScreenIntent.RateButtonClicked -> openRatingScreen()
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
      setRecipeSavedStatusUseCase(state.recipe.id, true)
    }
  }

  private suspend fun openRecipeMenu() = openChildScreen(RecipeScreenBottomSheetType.MENU)

  private suspend fun openRatingScreen() = openChildScreen(RecipeScreenBottomSheetType.RATING)

  private suspend fun openChildScreen(sheetType: RecipeScreenBottomSheetType) {
    updateRecipeStateSafely { state ->
      state.copy(bottomSheetType = sheetType)
    }
    _effect.emit(RecipeScreenEffect.OpenModalBottomSheet)
  }

  private suspend fun openPicturesViewer(
    selectedPicture: String = "",
  ) {
    (state.value as? RecipeScreenState.Success)?.recipe?.let { recipe ->
      val pictures = mutableListOf<String>()
      recipe.preview?.let { pictures.add(it) }
      recipe.cooking.onEach { item ->
        if (item is CookingItem.Step) pictures.addAll(item.pictures)
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
    val state = _state.getAndUpdate { state ->
      _effect.emit(RecipeScreenEffect.ShowToast(R.string.common_recipe_screen_ingredients_added_to_shopping_list))
      (state as? RecipeScreenState.Success)?.copy(selectedIngredients = emptySet()) ?: state
    } as? RecipeScreenState.Success ?: return

    val recipe = state.recipe as? DecryptedRecipe
    val servings = state.recipe.servings
    val multiplier = state.servingsMultiplier.toFloat() / (servings ?: 1)
    recipe?.ingredients
      ?.filterIsInstance<IngredientsItem.Ingredient>()
      ?.filter { it.id in state.selectedIngredients }
      ?.map { ingredient ->
        Purchase(
          id = ingredient.id,
          name = ingredient.name,
          amount = ingredient.amount?.let { amount -> amount * multiplier },
          measureUnit = ingredient.measureUnit,
          multiplier = multiplier.toInt(),
          recipeId = recipeId,
        )
      }
      ?.let { purchases ->
        getShoppingListsUseCase().onSuccess { shoppingLists ->
          shoppingLists
            .firstOrNull { it.type == ShoppingListMeta.Type.PERSONAL }?.id?.let { shoppingListId ->
              addToShoppingListUseCase(
                shoppingListId = shoppingListId,
                purchases = purchases,
                recipeNames = mapOf(recipe.id to recipe.name),
              )
            }
        }
      }
  }

  private suspend fun updateRecipeStateSafely(
    block: suspend (RecipeScreenState.Success) -> RecipeScreenState
  ) {
    _state.update { state ->
      (state as? RecipeScreenState.Success)?.let { block(it) } ?: state
    }
  }
}
