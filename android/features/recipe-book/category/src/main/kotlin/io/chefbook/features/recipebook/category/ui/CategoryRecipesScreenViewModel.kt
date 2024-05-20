package io.chefbook.features.recipebook.category.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenEffect
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CategoryRecipesScreenViewModel(
  private val categoryId: String,
  private val isTag: Boolean,

  private val observeRecipeBookUseCase: ObserveRecipeBookUseCase,
) : BaseMviViewModel<CategoryScreenState, CategoryScreenIntent, CategoryScreenEffect>() {

  override val _state: MutableStateFlow<CategoryScreenState> =
    MutableStateFlow(CategoryScreenState())

  init {
    viewModelScope.launch {
      observeRecipes()
    }
  }

  private suspend fun observeRecipes() {
    observeRecipeBookUseCase()
      .collect { recipeBook ->
        recipeBook?.let {
          val name: String?
          val emoji: String?
          if (isTag) {
            val tags = recipeBook.recipes.flatMap { it.tags }
            val tag = tags.find { it.id == categoryId }
            name = tag?.name
            emoji = tag?.emoji
          } else {
            val category = recipeBook.categories.find { it.id == categoryId }
            name = category?.name
            emoji = category?.emoji
          }
          _state.emit(
            CategoryScreenState(
              name = name,
              emoji,
              isEditButtonAvailable = !isTag,
              recipes = filterRecipes(recipeBook.recipes, categoryId)
            )
          )
        }
      }
  }

  override suspend fun reduceIntent(intent: CategoryScreenIntent) {
    when (intent) {
      is CategoryScreenIntent.OpenRecipeScreen -> _effect.emit(
        CategoryScreenEffect.OpenRecipeScreen(
          intent.recipeId
        )
      )

      is CategoryScreenIntent.OpenCategoryInputDialog -> _effect.emit(
        CategoryScreenEffect.OpenCategoryInputDialog(
          categoryId = categoryId
        )
      )

      is CategoryScreenIntent.OnCategoryUpdated ->
        _state.update { state ->
          state.copy(
            name = intent.category.name,
            emoji = intent.category.emoji,
          )
        }

      is CategoryScreenIntent.Back -> _effect.emit(CategoryScreenEffect.Back)
    }
  }

  private fun filterRecipes(
    recipes: List<RecipeInfo>,
    categoryId: String
  ): List<DecryptedRecipeInfo> = recipes
    .filter { recipe ->
      val ids = if (isTag) recipe.tags.map { it.id } else recipe.categories.map { it.id }
      categoryId in ids
    }
    .filterIsInstance<DecryptedRecipeInfo>()
    .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
}
