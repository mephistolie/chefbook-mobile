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

internal typealias ICategoryRecipesScreenViewModel = MviViewModel<CategoryScreenState, CategoryScreenIntent, CategoryScreenEffect>

internal class CategoryRecipesScreenViewModel(
  private val categoryId: String,

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
          _state.emit(
            CategoryScreenState(
              category = recipeBook.categories.find { it.id == categoryId },
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

      is CategoryScreenIntent.OnCategoryUpdated -> _state.update { it.copy(category = intent.category) }

      is CategoryScreenIntent.Back -> _effect.emit(CategoryScreenEffect.Back)
    }
  }

  private fun filterRecipes(
    recipes: List<RecipeInfo>,
    categoryId: String
  ): List<DecryptedRecipeInfo> =
    recipes.filter { recipe -> categoryId in recipe.categories.map { it.id } }
      .filterIsInstance<DecryptedRecipeInfo>()
      .sortedWith(compareBy({ it.name.uppercase() }, { it.id }))

}
