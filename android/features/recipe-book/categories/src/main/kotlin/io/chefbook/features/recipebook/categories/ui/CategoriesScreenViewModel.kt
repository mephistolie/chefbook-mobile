package io.chefbook.features.recipebook.categories.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipebook.categories.ui.mvi.CategoriesScreenEffect
import io.chefbook.features.recipebook.categories.ui.mvi.CategoriesScreenIntent
import io.chefbook.features.recipebook.categories.ui.mvi.CategoriesScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.usecases.GetTagsUseCase
import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class CategoriesScreenViewModel(
  private val observeRecipeBookUseCase: ObserveRecipeBookUseCase,
  private val observeTagsUseCase: ObserveTagsUseCase,
) :
  BaseMviViewModel<CategoriesScreenState, CategoriesScreenIntent, CategoriesScreenEffect>() {

  override val _state: MutableStateFlow<CategoriesScreenState> = MutableStateFlow(
    CategoriesScreenState()
  )

  init {
    viewModelScope.launch {
      observeTagsUseCase()
      observeRecipeBookUseCase()
        .collect { recipeBook ->
          _state.emit(
            CategoriesScreenState(
              categories = recipeBook?.categories.orEmpty(),
              tags = recipeBook?.recipes
                ?.flatMap { it.tags }
                ?.distinctBy { it.name }
                .orEmpty(),
            )
          )
        }
    }
  }

  override suspend fun reduceIntent(intent: CategoriesScreenIntent) {
    when (intent) {
      is CategoriesScreenIntent.CategoryClicked ->
        _effect.emit(CategoriesScreenEffect.CategoryOpened(intent.categoryId))
      is CategoriesScreenIntent.TagClicked ->
        _effect.emit(CategoriesScreenEffect.TagOpened(intent.tagId))

      is CategoriesScreenIntent.Back -> _effect.emit(CategoriesScreenEffect.Back)
    }
  }

}
