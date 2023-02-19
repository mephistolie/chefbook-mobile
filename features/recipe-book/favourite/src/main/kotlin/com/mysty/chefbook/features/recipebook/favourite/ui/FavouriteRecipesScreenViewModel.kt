package com.mysty.chefbook.features.recipebook.favourite.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenEffect
import com.mysty.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenIntent
import com.mysty.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal typealias IFavouriteRecipesScreenViewModel = MviViewModel<FavouriteRecipesScreenState, FavouriteRecipesScreenIntent, FavouriteRecipesScreenEffect>

internal class FavouriteRecipesScreenViewModel(
  private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
) :
  MviViewModel<FavouriteRecipesScreenState, FavouriteRecipesScreenIntent, FavouriteRecipesScreenEffect>() {

  override val _state: MutableStateFlow<FavouriteRecipesScreenState> = MutableStateFlow(
    FavouriteRecipesScreenState()
  )

  init {
    viewModelScope.launch {
      observeRecipeBookUseCase()
        .collect { recipes ->
          _state.emit(
            FavouriteRecipesScreenState(
              recipes
                ?.filter { it.isFavourite }
                ?.sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
            )
          )
        }
    }
  }

  override suspend fun reduceIntent(intent: FavouriteRecipesScreenIntent) {
    when (intent) {
      is FavouriteRecipesScreenIntent.OpenRecipeScreen -> _effect.emit(
        FavouriteRecipesScreenEffect.OnRecipeOpened(
          intent.recipeId
        )
      )
      is FavouriteRecipesScreenIntent.Back -> _effect.emit(FavouriteRecipesScreenEffect.Back)
    }
  }

}
