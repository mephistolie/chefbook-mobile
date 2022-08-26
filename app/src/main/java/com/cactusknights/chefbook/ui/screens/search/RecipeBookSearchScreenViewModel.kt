package com.cactusknights.chefbook.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.usecases.category.IObserveCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IObserveRecipeBookUseCase
import com.cactusknights.chefbook.ui.screens.search.models.RecipeBookSearchScreenEffect
import com.cactusknights.chefbook.ui.screens.search.models.RecipeBookSearchScreenEvent
import com.cactusknights.chefbook.ui.screens.search.models.RecipeBookSearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeBookSearchScreenViewModel @Inject constructor(
    private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
    private val observeCategoriesUseCase: IObserveCategoriesUseCase,
) : ViewModel(), EventHandler<RecipeBookSearchScreenEvent> {

    private val _state: MutableStateFlow<RecipeBookSearchScreenState> = MutableStateFlow(RecipeBookSearchScreenState())
    val state: StateFlow<RecipeBookSearchScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<RecipeBookSearchScreenEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<RecipeBookSearchScreenEffect> = _effect.asSharedFlow()

    init {
        observeRecipes()
        observeCategories()
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            observeRecipeBookUseCase()
                .onEach { recipes ->
                    val lastState = state.value
                    if (recipes != null && lastState.query.isNotEmpty()) {
                        val filteredRecipes = filterRecipes(recipes, lastState.query)
                        _state.emit(lastState.copy(recipes = filteredRecipes))
                    }
                }
                .collect()
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            observeCategoriesUseCase().onEach { categories ->
                val lastState = state.value
                if (categories != null && lastState.query.isNotEmpty()) {
                    val filteredCategories = filterCategories(categories, lastState.query)
                    _state.emit(lastState.copy(categories = filteredCategories))
                }
            }.collect()
        }
    }


    override fun obtainEvent(event: RecipeBookSearchScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeBookSearchScreenEvent.Search -> search(event.query)
                is RecipeBookSearchScreenEvent.OpenCategoryScreen -> _effect.emit(RecipeBookSearchScreenEffect.OnCategoryOpened(event.categoryId))
                is RecipeBookSearchScreenEvent.OpenRecipeScreen -> _effect.emit(RecipeBookSearchScreenEffect.OnRecipeOpened(event.recipeId))
                is RecipeBookSearchScreenEvent.Back -> _effect.emit(RecipeBookSearchScreenEffect.Back)
            }
        }
    }

    private suspend fun search(query: String) {
        if (query.isNotEmpty()) {
            _state.emit(state.value.copy(query = query, isLoading = true))
            val recipes = filterRecipes(observeRecipeBookUseCase().value ?: emptyList(), query)
            val categories = filterCategories(observeCategoriesUseCase().value ?: emptyList(), query)
            _state.emit(state.value.copy(isLoading = false, recipes = recipes, categories = categories))
        } else {
            _state.emit(RecipeBookSearchScreenState())
        }
    }

    private fun filterRecipes(recipes: List<RecipeInfo>, query: String): List<RecipeInfo> =
        recipes.filter { recipe -> query.lowercase() in recipe.name.lowercase() }.sortedBy { it.name }

    private fun filterCategories(categories: List<Category>, query: String): List<Category> =
        categories.filter { category -> query.lowercase() in category.name.lowercase() }.sortedBy { it.name }
}
