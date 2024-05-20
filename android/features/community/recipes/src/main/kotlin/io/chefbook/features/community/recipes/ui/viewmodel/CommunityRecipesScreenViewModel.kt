package io.chefbook.features.community.recipes.ui.viewmodel

import androidx.lifecycle.viewModelScope
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.features.community.recipes.ui.mvi.DashboardState
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesFilter
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesSorting
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetRecipesUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipesUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveCommunityRecipesLanguagesUseCase
import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityRecipesScreenViewModel(
  private val initialSearch: String = "",

  observeRecipesUseCase: ObserveRecipesUseCase,
  getRecipesUseCase: GetRecipesUseCase,
  observeProfileUseCase: ObserveProfileUseCase,
  observeLanguagesUseCase: ObserveCommunityRecipesLanguagesUseCase,
  observeTagsUseCase: ObserveTagsUseCase,
) :
  BaseMviViewModel<CommunityRecipesScreenState, CommunityRecipesScreenIntent, CommunityRecipesScreenEffect>() {

  override val _state = MutableStateFlow(
    CommunityRecipesScreenState(
      mode = if (initialSearch.isBlank()) CommunityRecipesScreenState.Mode.DASHBOARD else CommunityRecipesScreenState.Mode.FILTER,
      filter = FilterState(search = initialSearch.ifBlank { null }),
    )
  )

  private val requestsDelegate = CommunityRecipesScreenRequestsDelegate(
    updateState = _state::update,
    getRecipesFilter = { _state.value.toRecipeFilter() },
    scope = viewModelScope,
    getRecipesUseCase = getRecipesUseCase,
  )

  private val externalDelegate = CommunityRecipesScreenExternalDelegate(
    updateState = _state::update,
    resetResults = requestsDelegate::resetResults,
    scope = viewModelScope,
    observeProfileUseCase = observeProfileUseCase,
    observeLanguagesUseCase = observeLanguagesUseCase,
    observeRecipesUseCase = observeRecipesUseCase,
  )

  private val dashboardDelegate = CommunityRecipesScreenDashboardDelegate(
    getState = { state.value.dashboard },
    updateState = { update ->
      _state.update {
        val dashboard = update(it.dashboard)
        it.copy(
          dashboard = dashboard,
          filter = it.filter.copy(
            sorting = when (dashboard.tab) {
              DashboardState.Tab.NEW -> FilterState.Sorting.CREATION_TIMESTAMP
              DashboardState.Tab.VOTES -> FilterState.Sorting.VOTES
              DashboardState.Tab.TOP -> FilterState.Sorting.RATING
              DashboardState.Tab.FAST -> FilterState.Sorting.TIME
            }
          )
        )
      }
    },
    changeMode = { mode -> _state.update { it.copy(mode = mode) } },
    reduceIntentParent = this::reduceIntent,
    emitEffect = _effect::emit,
    resetResults = requestsDelegate::resetResults,
    scope = viewModelScope,
    observeTagsUseCase = observeTagsUseCase,
  )

  private val filterDelegate = CommunityRecipesScreenFilterDelegate(
    getState = { state.value.filter },
    updateState = { update ->
      _state.update {
        val filter = update(it.filter)
        it.copy(
          filter = update(it.filter),
          dashboard = it.dashboard.copy(
            tab = when (filter.sorting) {
              FilterState.Sorting.CREATION_TIMESTAMP -> DashboardState.Tab.NEW
              FilterState.Sorting.VOTES -> DashboardState.Tab.VOTES
              FilterState.Sorting.RATING -> DashboardState.Tab.TOP
              FilterState.Sorting.TIME -> DashboardState.Tab.FAST
              else -> it.dashboard.tab
            }
          )
        )
      }
    },
    changeMode = { mode -> _state.update { it.copy(mode = mode) } },
    emitEffect = _effect::emit,
    resetResults = requestsDelegate::resetResults,
    scope = viewModelScope,
    observeTagsUseCase = observeTagsUseCase,
  )

  init {
    viewModelScope.launch { requestsDelegate.loadNextRecipes() }
  }

  override suspend fun reduceIntent(intent: CommunityRecipesScreenIntent) {
    when (intent) {
      is CommunityRecipesScreenIntent.Content -> dashboardDelegate.reduceIntent(intent)
      is CommunityRecipesScreenIntent.Filter -> filterDelegate.reduceIntent(intent)

      is CommunityRecipesScreenIntent.Back ->
        when {
          state.value.mode == CommunityRecipesScreenState.Mode.FILTER && initialSearch.isBlank() -> {
            _state.update { it.copy(mode = CommunityRecipesScreenState.Mode.DASHBOARD) }
            requestsDelegate.resetResults()
            filterDelegate.reduceIntent(CommunityRecipesScreenIntent.Filter.ResetFilterClicked)
            filterDelegate.reduceIntent(CommunityRecipesScreenIntent.Filter.ConfirmFilterClicked)
          }

          else -> _effect.emit(CommunityRecipesScreenEffect.Back)
        }

      is CommunityRecipesScreenIntent.LanguagesButtonClicked ->
        _effect.emit(CommunityRecipesScreenEffect.LanguagesPickerOpened)

      is CommunityRecipesScreenIntent.ProfileButtonClicked ->
        _effect.emit(CommunityRecipesScreenEffect.ProfileScreenOpened)

      is CommunityRecipesScreenIntent.RecipesScrollEnded -> requestsDelegate.loadNextRecipes()
      is CommunityRecipesScreenIntent.RecipeCardClicked ->
        _effect.emit(CommunityRecipesScreenEffect.RecipeScreenOpened(intent.recipeId))
    }
  }
}

private fun CommunityRecipesScreenState.toRecipeFilter(): RecipesFilter {
  val lastRecipe = recipes.lastOrNull()

  return when (mode) {
    CommunityRecipesScreenState.Mode.DASHBOARD -> RecipesFilter(
      lastRecipe = lastRecipe,
      sorting = when (dashboard.tab) {
        DashboardState.Tab.NEW -> RecipesSorting.CREATION_TIMESTAMP
        DashboardState.Tab.VOTES -> RecipesSorting.VOTES
        DashboardState.Tab.TOP -> RecipesSorting.RATING
        DashboardState.Tab.FAST -> RecipesSorting.TIME
      }
    )

    CommunityRecipesScreenState.Mode.FILTER -> RecipesFilter(
      lastRecipe = lastRecipe,
      sorting = when (filter.sorting) {
        FilterState.Sorting.VOTES -> RecipesSorting.VOTES
        FilterState.Sorting.RATING -> RecipesSorting.RATING
        FilterState.Sorting.CREATION_TIMESTAMP -> RecipesSorting.CREATION_TIMESTAMP
        FilterState.Sorting.TIME -> RecipesSorting.TIME
        FilterState.Sorting.CALORIES -> RecipesSorting.CALORIES
      },

      tags = filter.selectedTags.ifEmpty { null },

      search = filter.search,

      minRating = if (filter.highRatingOnly) 4 else 0,

      minCalories = filter.minCalories,
      maxCalories = filter.maxCalories,
    )
  }
}