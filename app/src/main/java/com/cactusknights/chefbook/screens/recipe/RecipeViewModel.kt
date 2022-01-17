package com.cactusknights.chefbook.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.common.Utils.toPurchase
import com.cactusknights.chefbook.domain.usecases.CategoriesUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.domain.usecases.ShoppingListUseCases
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityState
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityViewEffect
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val categoriesUseCases: CategoriesUseCases,
    private val shoppingListUseCases: ShoppingListUseCases,
) : ViewModel(), EventHandler<RecipeActivityEvent> {

    private val _recipeState: MutableStateFlow<RecipeActivityState> = MutableStateFlow(RecipeActivityState.Loading)
    val recipeState: StateFlow<RecipeActivityState> = _recipeState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<RecipeActivityViewEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<RecipeActivityViewEffect> = _viewEffect.asSharedFlow()

    private var recipe : DecryptedRecipe = DecryptedRecipe()
    private var selectedIngredients: List<Selectable<String>> = listOf()
    private var categories = listOf<Category>()

    init {
        viewModelScope.launch {
            categoriesUseCases.listenToCategories().collect { newCategories ->
                categories = newCategories
                _recipeState.emit(RecipeActivityState.DataUpdated(recipe, newCategories, selectedIngredients))
            }
        }
    }

    override fun obtainEvent(event: RecipeActivityEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeActivityEvent.LoadRecipe -> loadRecipe(event.recipe)
                is RecipeActivityEvent.LoadRecipeByRemoteId -> loadRecipeByRemoteId(event.remoteId)
                is RecipeActivityEvent.AddRecipeToRecipeBook -> TODO()
                is RecipeActivityEvent.ChangeFavouriteStatus -> changeRecipeFavouriteStatus()
                is RecipeActivityEvent.ChangeLikeStatus -> changeRecipeLikeStatus()
                is RecipeActivityEvent.AddSelectedToShoppingList -> addSelectedToShoppingList()
                is RecipeActivityEvent.ChangeIngredientSelectStatus -> changeIngredientSelectedStatus(event.index)
                is RecipeActivityEvent.DeleteRecipe -> deleteRecipe()
                is RecipeActivityEvent.SetCategories -> {
                    recipe.categories = event.categories
                    recipeUseCases.setRecipeCategories(recipe).collect {}
                }
                RecipeActivityEvent.EditRecipe -> { _viewEffect.emit(RecipeActivityViewEffect.EditRecipe(recipe)) }
            }
        }
    }

    private suspend fun loadRecipe(loadedRecipe: DecryptedRecipe) {
        recipe = loadedRecipe
        selectedIngredients = loadedRecipe.ingredients.map { Selectable(it.text) }
        _recipeState.emit(RecipeActivityState.DataUpdated(recipe, categories, selectedIngredients))
    }

    private suspend fun loadRecipeByRemoteId(remoteId: Int) {
        recipeUseCases.getRecipeByRemoteId(remoteId).collect { state ->
            if (state is Result.Success) {
                recipe = state.data!! as DecryptedRecipe
                selectedIngredients = recipe.ingredients.map { Selectable(it.text) }
                _recipeState.emit(RecipeActivityState.DataUpdated(recipe, categories, selectedIngredients))
            }
        }
    }

    private suspend fun deleteRecipe() {
        recipeUseCases.deleteRecipe(recipe).collect { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> { _viewEffect.emit(RecipeActivityViewEffect.RecipeDeleted)}
                is Result.Error -> {}
            }
        }
    }

    private suspend fun changeRecipeFavouriteStatus() {
        recipe.isFavourite = !recipe.isFavourite
        _recipeState.emit(RecipeActivityState.DataUpdated(recipe, categories, selectedIngredients))
        recipeUseCases.setRecipeFavouriteStatus(recipe).collect {}
    }

    private suspend fun changeRecipeLikeStatus() {
        recipe.isLiked = !recipe.isLiked
        recipe.likes = if (recipe.isLiked) recipe.likes+1 else recipe.likes-1
        _recipeState.emit(RecipeActivityState.DataUpdated(recipe, categories, selectedIngredients))
        recipeUseCases.setRecipeLikeStatus(recipe).collect {}
    }

    private fun changeIngredientSelectedStatus(index: Int) {
        selectedIngredients[index].isSelected = !selectedIngredients[index].isSelected
    }

    private suspend fun addSelectedToShoppingList() {
        val confirmedIngredients = selectedIngredients.filter { it.isSelected }.map { it.isSelected = false; it } as ArrayList<Selectable<String>>
        shoppingListUseCases.addToShoppingList(confirmedIngredients.map { it.toPurchase() } as ArrayList<Purchase>).collect { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> { _viewEffect.emit(RecipeActivityViewEffect.Message(R.string.added_to_shopping_list)) }
                is Result.Error -> {}
            }
        }
    }
}