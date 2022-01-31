package com.cactusknights.chefbook.screens.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.*
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenViewEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.crypto.SecretKey
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val categoriesUseCases: CategoriesUseCases,
    private val shoppingListUseCases: ShoppingListUseCases,
    private val encryptionUseCases: EncryptionUseCases,
    private val commonUseCases: CommonUseCases
) : ViewModel(), EventHandler<RecipeScreenEvent> {

    private val _recipeState: MutableStateFlow<RecipeScreenState> = MutableStateFlow(RecipeScreenState.Loading)
    val recipeState: StateFlow<RecipeScreenState> = _recipeState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<RecipeScreenViewEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<RecipeScreenViewEffect> = _viewEffect.asSharedFlow()

    private var recipe : DecryptedRecipe = DecryptedRecipe()
    private var selectedIngredients: List<Selectable<String>> = listOf()
    private var key: SecretKey? = null
    private var categories = listOf<Category>()

    init {
        viewModelScope.launch {
            categoriesUseCases.listenToCategories().collect { newCategories ->
                categories = newCategories?: arrayListOf()
                _recipeState.emit(RecipeScreenState.DataUpdated(recipe, categories, selectedIngredients))
            }
        }
    }

    override fun obtainEvent(event: RecipeScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeScreenEvent.LoadRecipe -> loadRecipe(event.recipe, event.context)
                is RecipeScreenEvent.LoadRecipeByRemoteId -> loadRecipeByRemoteId(event.remoteId)
                is RecipeScreenEvent.AddRecipeToRecipeBook -> TODO()
                is RecipeScreenEvent.ChangeFavouriteStatus -> changeRecipeFavouriteStatus()
                is RecipeScreenEvent.ChangeLikeStatus -> changeRecipeLikeStatus()
                is RecipeScreenEvent.AddSelectedToShoppingList -> addSelectedToShoppingList()
                is RecipeScreenEvent.ChangeIngredientSelectStatus -> changeIngredientSelectedStatus(event.index)
                is RecipeScreenEvent.DecryptPicture -> { _viewEffect.emit(RecipeScreenViewEffect.EditRecipe(recipe)) }
                is RecipeScreenEvent.EditRecipe -> { _viewEffect.emit(RecipeScreenViewEffect.EditRecipe(recipe)) }
                is RecipeScreenEvent.DeleteRecipe -> deleteRecipe()
                is RecipeScreenEvent.SetCategories -> {
                    recipe.categories = event.categories
                    recipeUseCases.setRecipeCategories(recipe).collect {}
                }
            }
        }
    }

    private suspend fun loadRecipe(loadedRecipe: DecryptedRecipe, context: Context) {
        recipe = loadedRecipe
        selectedIngredients = loadedRecipe.ingredients.map { Selectable(it.text) }
        updateState()
        if (recipe.encrypted) {
            encryptionUseCases.getRecipeKey(recipe).collect { result ->
                if (result is Result.Success) {
                    key = result.data!!
                    updateState()
                }
            }
        }
    }

    private suspend fun loadRecipeByRemoteId(remoteId: Int) {
        recipeUseCases.getRecipeByRemoteId(remoteId).collect { state ->
            if (state is Result.Success) {
                recipe = state.data!! as DecryptedRecipe
                selectedIngredients = recipe.ingredients.map { Selectable(it.text) }
                updateState()
            }
        }
    }

    private suspend fun deleteRecipe() {
        recipeUseCases.deleteRecipe(recipe).collect { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> { _viewEffect.emit(RecipeScreenViewEffect.RecipeDeleted)}
                is Result.Error -> {}
            }
        }
    }

    private suspend fun changeRecipeFavouriteStatus() {
        recipe.isFavourite = !recipe.isFavourite
        updateState()
        recipeUseCases.setRecipeFavouriteStatus(recipe).collect {}
    }

    private suspend fun changeRecipeLikeStatus() {
        recipe.isLiked = !recipe.isLiked
        recipe.likes = if (recipe.isLiked) recipe.likes+1 else recipe.likes-1
        updateState()
        recipeUseCases.setRecipeLikeStatus(recipe).collect {}
    }

    private suspend fun changeIngredientSelectedStatus(index: Int) {
        selectedIngredients[index].isSelected = !selectedIngredients[index].isSelected
        updateState()
    }

    private suspend fun addSelectedToShoppingList() {
        val confirmedIngredients = selectedIngredients.filter { it.isSelected }.map { it.toPurchase() }
        shoppingListUseCases.addToShoppingList(confirmedIngredients).collect { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    selectedIngredients = selectedIngredients.map { it.isSelected = false; it }
                    _recipeState.emit(RecipeScreenState.DataUpdated(recipe, categories, selectedIngredients))
                    _viewEffect.emit(RecipeScreenViewEffect.Message(R.string.added_to_shopping_list))
                }
                is Result.Error -> {}
            }
        }
    }

    private suspend fun updateState() {
        _recipeState.emit(RecipeScreenState.DataUpdated(recipe, categories, selectedIngredients, key))
    }
}