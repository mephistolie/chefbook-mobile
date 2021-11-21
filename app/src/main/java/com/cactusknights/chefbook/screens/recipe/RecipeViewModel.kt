package com.cactusknights.chefbook.screens.recipe

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.BaseViewModel
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.domain.usecases.ShoppingListUseCases
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val shoppingListUseCases: ShoppingListUseCases
) : BaseViewModel<RecipeActivityState>(RecipeActivityState()) {

    fun setRecipe(recipe: DecryptedRecipe) {
        viewModelScope.launch {
            _state.emit(RecipeActivityState(
                recipe = recipe,
                selectedIngredients = recipe.ingredients.map { Selectable(it.text) } as ArrayList<Selectable<String>>
            ))
        }
    }

    fun deleteRecipe() {
        viewModelScope.launch {
            recipeUseCases.deleteRecipe(state.value.recipe).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _state.emit(RecipeActivityState(
                            message = R.string.recipe_deleted,
                            isDeleted = true
                        ))
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    fun changeRecipeFavouriteStatus() {
        state.value.recipe.isFavourite = !state.value.recipe.isFavourite
        viewModelScope.launch {
            recipeUseCases.setRecipeFavouriteStatus(state.value.recipe).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {}
                    is Result.Error -> {}
                }
            }
        }
    }

    fun changeIngredientSelectedStatus(index: Int) {
        state.value.selectedIngredients[index].isSelected = !state.value.selectedIngredients[index].isSelected
        viewModelScope.launch {
            _state.emit(RecipeActivityState(
                recipe = state.value.recipe,
                selectedIngredients = state.value.selectedIngredients
            ))
        }
    }

    fun addSelectedToShoppingList() {
        viewModelScope.launch {
            val selectedIngredients = state.value.selectedIngredients.filter { it.isSelected }.map { it.isSelected = false; it } as ArrayList<Selectable<String>>
            shoppingListUseCases.addToShoppingList(selectedIngredients).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _state.emit(RecipeActivityState(
                            recipe = state.value.recipe,
                            selectedIngredients = state.value.recipe.ingredients.map { Selectable(it.text) } as ArrayList<Selectable<String>>,
                            message = R.string.added_to_shopping_list
                        ))
                    }
                    is Result.Error -> {}
                }
            }
        }
    }
}