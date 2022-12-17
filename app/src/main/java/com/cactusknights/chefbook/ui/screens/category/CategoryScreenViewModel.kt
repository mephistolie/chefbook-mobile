package com.cactusknights.chefbook.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.category.domain.entities.toCategory
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.category.domain.usecases.IDeleteCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IUpdateCategoryUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenEffect
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenEvent
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenState
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CategoryScreenViewModel(
    private val getCategoryUseCase: IGetCategoryUseCase,
    private val updateCategoryUseCase: IUpdateCategoryUseCase,
    private val deleteCategoryUseCase: IDeleteCategoryUseCase,
    private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
) : ViewModel(), EventHandler<CategoryScreenEvent> {

    private val _state: MutableStateFlow<CategoryScreenState> = MutableStateFlow(CategoryScreenState())
    val state: StateFlow<CategoryScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<CategoryScreenEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<CategoryScreenEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            observeRecipeBookUseCase()
                .onEach { recipes ->
                    val lastState = state.value
                    if (recipes != null && lastState.category != null) {
                        val filteredRecipes = filterRecipes(recipes, lastState.category.id)
                        _state.emit(lastState.copy(recipes = filteredRecipes))
                    }
                }
                .collect()
        }
    }

    override fun obtainEvent(event: CategoryScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is CategoryScreenEvent.LoadRecipesInCategory -> loadRecipesInCategory(event.categoryId)
                is CategoryScreenEvent.OpenRecipeScreen -> _effect.emit(CategoryScreenEffect.OnRecipeOpened(event.recipeId))
                is CategoryScreenEvent.ChangeDialogState -> updateDialogsState(event)
                is CategoryScreenEvent.SaveEditCategoryDialogState -> setCachedCategoryInput(event.input)
                is CategoryScreenEvent.EditCategory -> editCategory(event.input)
                is CategoryScreenEvent.DeleteCategory -> deleteCategory()
                is CategoryScreenEvent.Back -> _effect.emit(CategoryScreenEffect.Back)
            }
        }
    }

    private suspend fun loadRecipesInCategory(categoryId: String) {
        getCategoryUseCase(categoryId)
            .onEach { result ->
                if (result.isSuccess()) {
                    val category = result.data()
                    val recipes = filterRecipes(observeRecipeBookUseCase().value.orEmpty(), categoryId)
                    _state.emit(CategoryScreenState(category = category, recipes = recipes))
                }
            }
            .collect()
    }


    private suspend fun setCachedCategoryInput(input: CategoryInput?) {
        val lastState = state.value
        lastState.category?.let {
            _state.emit(lastState.copy(cachedCategoryInput = input))
        }
    }

    private suspend fun editCategory(input: CategoryInput) {
        val lastState = state.value
        lastState.category?.let { category ->
            _state.emit(lastState.copy(
                category = input.toCategory(category.id),
                cachedCategoryInput = null,
            ))
            updateCategoryUseCase(category.id, input)
                .onEach { result ->
                    if (result is Failure) _state.emit(lastState)
                    updateDialogsState(CategoryScreenEvent.ChangeDialogState.Edit(false))
                }
                .collect()
        }
    }

    private suspend fun deleteCategory() {
        val lastState = state.value
        lastState.category?.let { category ->
            deleteCategoryUseCase(category.id)
                .onEach { result ->
                    if (result.isSuccess()) {
                        _state.emit(lastState.copy(isEditCategoryDialogVisible = false, isDeleteCategoryDialogVisible = false))
                        _effect.emit(CategoryScreenEffect.Back)
                    }
                }
                .collect()
        }
    }

    private suspend fun updateDialogsState(event: CategoryScreenEvent.ChangeDialogState) {
        val lastState = state.value
        lastState.category?.let {
            val newState = when (event) {
                is CategoryScreenEvent.ChangeDialogState.Edit -> lastState.copy(isEditCategoryDialogVisible = event.isVisible)
                is CategoryScreenEvent.ChangeDialogState.Delete -> lastState.copy(isDeleteCategoryDialogVisible = event.isVisible)
            }

            _state.emit(newState)
        }
    }

    private fun filterRecipes(recipes: List<RecipeInfo>, categoryId: String): List<RecipeInfo> =
        recipes.filter { recipe -> categoryId in recipe.categories.map { it.id } }
            .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))

}
