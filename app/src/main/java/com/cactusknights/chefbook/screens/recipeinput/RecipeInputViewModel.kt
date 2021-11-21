package com.cactusknights.chefbook.screens.recipeinput

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.BaseViewModel
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.CategoriesUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeInputViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val categoriesUseCases: CategoriesUseCases
) : BaseViewModel<RecipeInputActivityState>(RecipeInputActivityState()) {

    init {
        viewModelScope.launch {
            categoriesUseCases.getCategoriesList().collect { result ->
                if (result is Result.Success) {
                    _state.emit(
                        RecipeInputActivityState(
                            recipe = state.value.recipe,
                            categories = result.data!!,
                            selectedCategories = result.data.map { Selectable(it.id,
                                it.id in state.value.recipe.categories
                            ) } as ArrayList<Selectable<Int>>,
                        )
                    )
                }
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoriesUseCases.addCategory(category).collect { result ->
                if (result is Result.Success) {
                    state.value.categories.add(result.data!!)
                    state.value.selectedCategories.add(Selectable(result.data.id))
                    _state.emit(
                        RecipeInputActivityState(
                            recipe = state.value.recipe,
                            categories = state.value.categories,
                            selectedCategories = state.value.selectedCategories
                        )
                    )
                }
            }
        }
    }

    fun changeCategoryCheckedStatus(position: Int) {
        state.value.selectedCategories[position].isSelected = !state.value.selectedCategories[position].isSelected
    }

    fun setRecipe(recipe: DecryptedRecipe) {
        viewModelScope.launch {
            _state.emit(
                RecipeInputActivityState(
                    recipe = recipe,
                    categories = state.value.categories,
                    selectedCategories = state.value.selectedCategories.map { it.isSelected = it.item in state.value.recipe.categories; it } as ArrayList<Selectable<Int>>
                )
            )
        }
    }

    fun setRecipeTime(hours: String, minutes: String) {
        var time = (hours.toIntOrNull() ?: 0) * 60
        time += minutes.toIntOrNull() ?: 0
        state.value.recipe.time = time
    }

    fun commitInput() {
        viewModelScope.launch {
            state.value.recipe.categories = state.value.selectedCategories.filter{ it.isSelected }.map { it.item!! } as ArrayList<Int>
            if (state.value.recipe.remoteId == null) addRecipe(state.value.recipe) else updateRecipe(
                state.value.recipe
            )
        }
    }

    private suspend fun addRecipe(recipe: DecryptedRecipe) {
        recipeUseCases.addRecipe(recipe).collect { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    _state.emit(
                        RecipeInputActivityState(
                            recipe = recipe,
                            isDone = true
                        )
                    )
                }
                is Result.Error -> {
                }
            }
        }
    }

    private suspend fun updateRecipe(recipe: DecryptedRecipe) {
        recipe.remoteId != null
        recipeUseCases.updateRecipe(recipe).collect { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    _state.emit(
                        RecipeInputActivityState(
                            recipe = recipe,
                            isDone = true
                        )
                    )
                }
                is Result.Error -> {
                }
            }
        }
    }
}