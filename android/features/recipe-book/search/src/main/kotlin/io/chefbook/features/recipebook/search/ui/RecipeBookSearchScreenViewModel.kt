package io.chefbook.features.recipebook.search.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.GetRecipeBookUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenEffect
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenIntent
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenState
import io.chefbook.sdk.category.api.external.domain.usecases.ObserveCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IRecipeBookSearchScreenViewModel = MviViewModel<RecipeBookSearchScreenState, RecipeBookSearchScreenIntent, RecipeBookSearchScreenEffect>

internal class RecipeBookSearchScreenViewModel(
  private val observeRecipeBookUseCase: ObserveRecipeBookUseCase,
  private val getRecipeBookUseCase: GetRecipeBookUseCase,
  private val observeCategoriesUseCase: ObserveCategoriesUseCase,
) :
  BaseMviViewModel<RecipeBookSearchScreenState, RecipeBookSearchScreenIntent, RecipeBookSearchScreenEffect>() {

  override val _state: MutableStateFlow<RecipeBookSearchScreenState> =
    MutableStateFlow(RecipeBookSearchScreenState())

  init {
    observeResults()
  }

  private fun observeResults() {
    viewModelScope.launch {
      observeRecipeBookUseCase()
        .collect { recipeBook ->
          val lastState = state.value
          if (recipeBook != null && lastState.query.isNotEmpty()) {
            _state.emit(
              lastState.copy(
                recipes = filterRecipes(recipeBook.recipes, lastState.query),
                categories = filterCategories(recipeBook.categories, lastState.query)
              )
            )
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
      val recipeBook = getRecipeBookUseCase()
      _state.emit(
        state.value.copy(
          isLoading = false,
          recipes = filterRecipes(recipeBook.recipes, query),
          categories = filterCategories(recipeBook.categories, query),
        )
      )
    } else {
      _state.emit(RecipeBookSearchScreenState())
    }
  }

  private fun filterRecipes(recipes: List<RecipeInfo>, query: String): List<DecryptedRecipeInfo> =
    recipes
      .filterIsInstance<DecryptedRecipeInfo>()
      .filter { recipe -> query.lowercase() in recipe.name.lowercase() }
      .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))

  private fun filterCategories(
    categories: List<io.chefbook.sdk.category.api.external.domain.entities.Category>,
    query: String
  ): List<io.chefbook.sdk.category.api.external.domain.entities.Category> =
    categories
      .filter { category -> query.lowercase() in category.name.lowercase() }
      .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
}
