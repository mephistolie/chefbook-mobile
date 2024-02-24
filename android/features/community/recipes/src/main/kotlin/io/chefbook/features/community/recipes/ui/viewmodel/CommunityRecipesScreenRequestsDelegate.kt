package io.chefbook.features.community.recipes.ui.viewmodel

import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.features.community.recipes.ui.mvi.DashboardState
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesFilter
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetRecipesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class CommunityRecipesScreenRequestsDelegate(
  private val updateState: ((CommunityRecipesScreenState) -> CommunityRecipesScreenState) -> Unit,
  private val getRecipesFilter: () -> RecipesFilter,

  private val scope: CoroutineScope,
  private val getRecipesUseCase: GetRecipesUseCase,
) {

  private var loadRecipesJob: Job? = null
  private val loadRecipesMutex = Mutex()

  private var isLoading: Boolean = false
  private var allRecipesLoaded: Boolean = false

  suspend fun resetResults() {
    loadRecipesJob?.cancel()
    updateState { it.copy(recipes = emptyList(), isLoading = false) }
    loadRecipesMutex.withLock {
      isLoading = false
      allRecipesLoaded = false
    }
    loadNextRecipes()
  }

  suspend fun loadNextRecipes() {
    loadRecipesMutex.withLock {
      if (isLoading || allRecipesLoaded) return
      isLoading = true
    }

    updateState { it.copy(isLoading = true) }
    val filter = getRecipesFilter()

    loadRecipesJob?.cancel()
    loadRecipesJob = scope.launch {
      val result = getRecipesUseCase(filter = filter)

      if (!isActive) return@launch

      result
        .onSuccess { recipes ->
          updateState { state ->
            val concatRecipes = state.recipes + recipes
            state.copy(
              recipes = concatRecipes,
              isLoading = false,
              dashboard = state.dashboard.copy(
                isChefMatchButtonVisible = if (concatRecipes.isNotEmpty()) {
                  true
                } else {
                  state.dashboard.isChefMatchButtonVisible
                },
              )
            )
          }
          if (recipes.isEmpty()) allRecipesLoaded = true
        }
        .onFailure { updateState { it.copy(isLoading = false) } }

      loadRecipesMutex.withLock {
        isLoading = false
        if (result.getOrNull()
            .orEmpty().size < RecipesFilter.DEFAULT_RECIPES_COUNT
        ) allRecipesLoaded = true
      }

      loadRecipesJob = null
    }
  }
}