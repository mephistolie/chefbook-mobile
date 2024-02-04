package io.chefbook.features.category.ui.input

import androidx.lifecycle.viewModelScope
import io.chefbook.features.category.ui.input.mvi.CategoryInputDialogEffect
import io.chefbook.features.category.ui.input.mvi.CategoryInputDialogIntent
import io.chefbook.features.category.ui.input.mvi.CategoryInputDialogState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.api.external.domain.entities.toInput
import io.chefbook.sdk.category.api.external.domain.usecases.CreateCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.DeleteCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.UpdateCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal typealias ICategoryInputDialogViewModel = MviViewModel<CategoryInputDialogState, CategoryInputDialogIntent, CategoryInputDialogEffect>

internal class CategoryInputDialogViewModel(
  private val categoryId: String?,

  private val getCategoryUseCase: GetCategoryUseCase,
  private val createCategoryUseCase: CreateCategoryUseCase,
  private val updateCategoryUseCase: UpdateCategoryUseCase,
  private val deleteCategoryUseCase: DeleteCategoryUseCase,
) :
  BaseMviViewModel<CategoryInputDialogState, CategoryInputDialogIntent, CategoryInputDialogEffect>() {

  override val _state: MutableStateFlow<CategoryInputDialogState> =
    MutableStateFlow(CategoryInputDialogState(isEditing = categoryId != null))

  init {
    categoryId?.let {
      viewModelScope.launch {
        getCategoryUseCase(categoryId).onSuccess { category ->
          _state.emit(state.value.copy(input = category.toInput()))
        }
      }
    }
  }

  override suspend fun reduceIntent(intent: CategoryInputDialogIntent) {
    when (intent) {
      is CategoryInputDialogIntent.Cancel -> _effect.emit(CategoryInputDialogEffect.Cancel)
      is CategoryInputDialogIntent.SetName -> setName(name = intent.name)
      is CategoryInputDialogIntent.SetCover -> setCover(newCover = intent.cover)
      is CategoryInputDialogIntent.ConfirmInput -> confirmInput()
      is CategoryInputDialogIntent.Delete -> _effect.emit(CategoryInputDialogEffect.OpenDeleteConfirmation)
      is CategoryInputDialogIntent.ConfirmDelete -> deleteCategory()
    }
  }

  private fun setName(name: String) {
    val formattedName = if (name.length > MAX_NAME_LENGTH) name.substring(0, MAX_NAME_LENGTH) else name
    _state.update { state -> state.copy(input = state.input.copy(name = formattedName)) }
  }

  private suspend fun setCover(newCover: String) {
    val lastCover = state.value.input.emoji.orEmpty()
    if (
      (lastCover.length > newCover.length || lastCover.isEmpty()) &&
      (newCover.length <= 10 && !coverRegex.matches(newCover) || newCover.length <= 1)
    ) {
      _state.update { state -> state.copy(input = state.input.copy(emoji = newCover.ifEmpty { null })) }
    }

  }

  private suspend fun confirmInput() {
    var input = state.value.input
    input = input.copy(name = input.name.trim())
    if (categoryId != null) updateCategory(
      categoryId = categoryId,
      input = input
    ) else createCategory(input = input)
  }

  private suspend fun createCategory(
    input: CategoryInput
  ) {
    createCategoryUseCase(input = input).onSuccess { category ->
      _effect.emit(CategoryInputDialogEffect.CategoryCreated(category))
    }
  }

  private suspend fun updateCategory(
    categoryId: String,
    input: CategoryInput,
  ) {
    updateCategoryUseCase(categoryId = categoryId, input = input).onSuccess { category ->
      _effect.emit(CategoryInputDialogEffect.CategoryUpdated(category))
    }
  }

  private suspend fun deleteCategory() {
    categoryId?.let {
      deleteCategoryUseCase(categoryId = categoryId).onSuccess {
        _effect.emit(CategoryInputDialogEffect.CategoryDeleted(categoryId))
      }
    }
  }

  companion object {
    private const val MAX_NAME_LENGTH = 64

    private val coverRegex = Regex("^.*[а-яА-ЯёЁa-zA-Z0-9!@#\$%ˆ&*()_+-=\"№;%:?*]+?")
  }
}
