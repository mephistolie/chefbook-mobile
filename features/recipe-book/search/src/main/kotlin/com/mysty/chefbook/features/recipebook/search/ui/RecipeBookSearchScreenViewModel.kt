package com.mysty.chefbook.features.recipebook.search.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoriesUseCase
import com.mysty.chefbook.api.category.domain.usecases.IObserveCategoriesUseCase
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.usecases.IGetRecipeBookUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenEffect
import com.mysty.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenIntent
import com.mysty.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IRecipeBookSearchScreenViewModel = IMviViewModel<RecipeBookSearchScreenState, RecipeBookSearchScreenIntent, RecipeBookSearchScreenEffect>

internal class RecipeBookSearchScreenViewModel(
  private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
  private val getRecipeBookUseCase: IGetRecipeBookUseCase,
  private val observeCategoriesUseCase: IObserveCategoriesUseCase,
  private val getCategoriesUseCase: IGetCategoriesUseCase,
) :
  MviViewModel<RecipeBookSearchScreenState, RecipeBookSearchScreenIntent, RecipeBookSearchScreenEffect>() {

  override val _state: MutableStateFlow<RecipeBookSearchScreenState> =
    MutableStateFlow(RecipeBookSearchScreenState())

  init {
    observeRecipes()
    observeCategories()
  }

  private fun observeRecipes() {
    viewModelScope.launch {
      observeRecipeBookUseCase()
        .collect { recipes ->
          val lastState = state.value
          if (recipes != null && lastState.query.isNotEmpty()) {
            val filteredRecipes = filterRecipes(recipes, lastState.query)
            _state.emit(lastState.copy(recipes = filteredRecipes))
          }
        }
    }
  }

  private fun observeCategories() {
    viewModelScope.launch {
      observeCategoriesUseCase()
        .collect { categories ->
          val lastState = state.value
          if (categories != null && lastState.query.isNotEmpty()) {
            val filteredCategories = filterCategories(categories, lastState.query)
            _state.emit(lastState.copy(categories = filteredCategories))
          }
        }
    }
  }


  override suspend fun reduceIntent(intent: RecipeBookSearchScreenIntent) {
    when (intent) {
      is RecipeBookSearchScreenIntent.Search -> search(intent.query)
      is RecipeBookSearchScreenIntent.OpenCategoryScreen -> _effect.emit(
        RecipeBookSearchScreenEffect.OnCategoryOpened(intent.categoryId)
      )
      is RecipeBookSearchScreenIntent.OpenRecipeScreen -> _effect.emit(
        RecipeBookSearchScreenEffect.OnRecipeOpened(
          intent.recipeId
        )
      )
      is RecipeBookSearchScreenIntent.Back -> _effect.emit(RecipeBookSearchScreenEffect.Back)
    }
  }

  private suspend fun search(query: String) {
    if (query.isNotEmpty()) {
      _state.emit(state.value.copy(query = query, isLoading = true))
      val recipes = filterRecipes(getRecipeBookUseCase(), query)
      val categories = filterCategories(getCategoriesUseCase(), query)
      _state.emit(state.value.copy(isLoading = false, recipes = recipes, categories = categories))
    } else {
      _state.emit(RecipeBookSearchScreenState())
    }
  }

  private fun filterRecipes(recipes: List<RecipeInfo>, query: String): List<RecipeInfo> =
    recipes
      .filter { recipe -> query.lowercase() in recipe.name.lowercase() }
      .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))

  private fun filterCategories(categories: List<Category>, query: String): List<Category> =
    categories
      .filter { category -> query.lowercase() in category.name.lowercase() }
      .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
}
