package io.chefbook.features.recipebook.favourite.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenEffect
import io.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenIntent
import io.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IFavouriteRecipesScreenViewModel = BaseMviViewModel<FavouriteRecipesScreenState, FavouriteRecipesScreenIntent, FavouriteRecipesScreenEffect>

internal class FavouriteRecipesScreenViewModel(
  private val observeRecipeBookUseCase: ObserveRecipeBookUseCase,
) :
  BaseMviViewModel<FavouriteRecipesScreenState, FavouriteRecipesScreenIntent, FavouriteRecipesScreenEffect>() {

  override val _state: MutableStateFlow<FavouriteRecipesScreenState> = MutableStateFlow(
    FavouriteRecipesScreenState()
  )

  init {
    viewModelScope.launch {
      observeRecipeBookUseCase()
        .collect { recipeBook ->
          _state.emit(
            FavouriteRecipesScreenState(
              recipeBook?.recipes
                ?.filterIsInstance<DecryptedRecipeInfo>()
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
