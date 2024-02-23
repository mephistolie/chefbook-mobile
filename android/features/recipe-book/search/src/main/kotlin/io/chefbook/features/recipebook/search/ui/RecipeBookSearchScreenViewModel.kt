package io.chefbook.features.recipebook.search.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenEffect
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenIntent
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.category.api.external.domain.usecases.ObserveCategoriesUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.GetRecipeBookUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal typealias IRecipeBookSearchScreenViewModel = MviViewModel<RecipeBookSearchScreenState, RecipeBookSearchScreenIntent, RecipeBookSearchScreenEffect>

internal class RecipeBookSearchScreenViewModel(
  private val observeProfileUseCase: ObserveProfileUseCase,
  private val observeRecipeBookUseCase: ObserveRecipeBookUseCase,
  private val getRecipeBookUseCase: GetRecipeBookUseCase,
  private val observeCategoriesUseCase: ObserveCategoriesUseCase,
) :
  BaseMviViewModel<RecipeBookSearchScreenState, RecipeBookSearchScreenIntent, RecipeBookSearchScreenEffect>() {

  override val _state: MutableStateFlow<RecipeBookSearchScreenState> =
    MutableStateFlow(RecipeBookSearchScreenState())

  init {
    observeProfile()
    observeResults()
  }

  private fun observeProfile() {
    observeProfileUseCase().collectState { state, profile ->
      state.copy(showCommunitySearchHint = profile?.isOnline ?: false)
    }
  }

  private fun observeResults() {
    observeRecipeBookUseCase()
      .collectInViewModelScope { recipeBook ->
        if (recipeBook != null && state.value.query.isNotEmpty()) {
          _state.update {
            it.copy(
              recipes = filterRecipes(recipeBook.recipes, it.query),
              categories = filterCategories(recipeBook.categories, it.query)
            )
          }
        }
      }
  }


  override suspend fun reduceIntent(intent: RecipeBookSearchScreenIntent) {
    when (intent) {
      is RecipeBookSearchScreenIntent.Search -> search(intent.query)
      is RecipeBookSearchScreenIntent.OpenCategoryScreen ->
        _effect.emit(RecipeBookSearchScreenEffect.OnCategoryOpened(intent.categoryId))

      is RecipeBookSearchScreenIntent.OpenRecipeScreen ->
        _effect.emit(RecipeBookSearchScreenEffect.OnRecipeOpened(intent.recipeId))

      is RecipeBookSearchScreenIntent.SearchInCommunity ->
        _effect.emit(RecipeBookSearchScreenEffect.CommunitySearchScreenOpened(state.value.query))

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
      _state.update { RecipeBookSearchScreenState(showCommunitySearchHint = it.showCommunitySearchHint) }
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
