package io.chefbook.features.recipe.rating.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipe.rating.ui.mvi.RecipeRatingScreenEffect
import io.chefbook.features.recipe.rating.ui.mvi.RecipeRatingScreenIntent
import io.chefbook.features.recipe.rating.ui.mvi.RecipeRatingScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipeUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeScoreUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class RecipeRatingScreenViewModel(
  private val recipeId: String,

  private val observeRecipeUseCase: ObserveRecipeUseCase,
  private val setRecipeScoreUseCase: SetRecipeScoreUseCase,
) :
  BaseMviViewModel<RecipeRatingScreenState, RecipeRatingScreenIntent, RecipeRatingScreenEffect>() {

  override val _state: MutableStateFlow<RecipeRatingScreenState> =
    MutableStateFlow(RecipeRatingScreenState())

  private var rateJob: Job? = null

  init {
    viewModelScope.launch {
      observeRecipe(recipeId)
    }
  }

  private suspend fun observeRecipe(recipeId: String) {
    observeRecipeUseCase.invoke(recipeId = recipeId)
      .catch { _effect.emit(RecipeRatingScreenEffect.Close) }
      .collect { recipe ->
        _state.update { state ->
          state.copy(
            rating = recipe?.rating ?: RecipeMeta.Rating(),
            isScoreVisible = recipe?.isOwned == false,
          )
        }
      }
  }

  override suspend fun reduceIntent(intent: RecipeRatingScreenIntent) {
    when (intent) {
      is RecipeRatingScreenIntent.ScoreClicked -> onScoreClicked(intent.score)
    }
  }

  private suspend fun onScoreClicked(score: Int) {
    rateJob?.cancel()
    rateJob = viewModelScope.launch {
      val finalScore = if (state.value.rating.score != score) score else null
      setRecipeScoreUseCase(recipeId, finalScore)
    }
  }
}
