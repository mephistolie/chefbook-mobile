package com.mysty.chefbook.features.recipebook.category.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoryUseCase
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenEffect
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal typealias ICategoryRecipesScreenViewModel = IMviViewModel<CategoryScreenState, CategoryScreenIntent, CategoryScreenEffect>

internal class CategoryRecipesScreenViewModel(
    private val categoryId: String,

    private val getCategoryUseCase: IGetCategoryUseCase,
    private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
) : MviViewModel<CategoryScreenState, CategoryScreenIntent, CategoryScreenEffect>() {

    override val _state: MutableStateFlow<CategoryScreenState> = MutableStateFlow(CategoryScreenState())

    init {
        viewModelScope.launch {
            loadCategory()
            observeRecipes()
        }
    }

    private suspend fun loadCategory() {
        getCategoryUseCase(categoryId)
            .collect { result ->
                if (result.isSuccess()) _state.emit(CategoryScreenState(category = result.data()))
            }
    }

    private suspend fun observeRecipes() {
        observeRecipeBookUseCase()
            .collect { recipes ->
                val lastState = state.value
                if (recipes != null && lastState.category != null) {
                    val filteredRecipes = filterRecipes(recipes, categoryId)
                    _state.emit(lastState.copy(recipes = filteredRecipes))
                }
            }
    }

    override suspend fun reduceIntent(intent: CategoryScreenIntent) {
        when (intent) {
            is CategoryScreenIntent.OpenRecipeScreen -> _effect.emit(CategoryScreenEffect.OpenRecipeScreen(intent.recipeId))
            is CategoryScreenIntent.OpenCategoryInputDialog -> _effect.emit(CategoryScreenEffect.OpenCategoryInputDialog(categoryId = categoryId))
            is CategoryScreenIntent.OnCategoryUpdated -> withSafeState { state -> _state.emit(state.copy(category = intent.category)) }
            is CategoryScreenIntent.Back -> _effect.emit(CategoryScreenEffect.Back)
        }
    }

    private fun filterRecipes(recipes: List<RecipeInfo>, categoryId: String): List<RecipeInfo> =
        recipes.filter { recipe -> categoryId in recipe.categories.map { it.id } }
            .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))

}
