package io.chefbook.features.category.ui.input

import androidx.lifecycle.viewModelScope
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenEffect
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenIntent
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.api.external.domain.entities.toInput
import io.chefbook.sdk.category.api.external.domain.usecases.CreateCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.DeleteCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.UpdateCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CategoryInputScreenViewModel(
  private val categoryId: String?,

  private val getCategoryUseCase: GetCategoryUseCase,
  private val createCategoryUseCase: CreateCategoryUseCase,
  private val updateCategoryUseCase: UpdateCategoryUseCase,
  private val deleteCategoryUseCase: DeleteCategoryUseCase,
) :
  BaseMviViewModel<CategoryInputScreenState, CategoryInputScreenIntent, CategoryInputScreenEffect>() {

  override val _state: MutableStateFlow<CategoryInputScreenState> =
    MutableStateFlow(CategoryInputScreenState(isEditing = categoryId != null))

  init {
    categoryId?.let {
      viewModelScope.launch {
        getCategoryUseCase(categoryId).onSuccess { category ->
          _state.emit(state.value.copy(input = category.toInput()))
        }
      }
    }
  }

  override suspend fun reduceIntent(intent: CategoryInputScreenIntent) {
    when (intent) {
      is CategoryInputScreenIntent.Cancel -> {
        val state = state.value
        val isProcessing = state.isSaving || state.isDeleting
        if (!isProcessing) _effect.emit(CategoryInputScreenEffect.Cancel)
      }

      is CategoryInputScreenIntent.SetName -> setName(name = intent.name)
      is CategoryInputScreenIntent.SetCover -> setCover(newCover = intent.cover)
      is CategoryInputScreenIntent.ConfirmInput -> confirmInput()
      is CategoryInputScreenIntent.Delete -> _effect.emit(CategoryInputScreenEffect.OpenDeleteConfirmation)
      is CategoryInputScreenIntent.ConfirmDelete -> deleteCategory()
    }
  }

  private fun setName(name: String) {
    val formattedName =
      if (name.length > MAX_NAME_LENGTH) name.substring(0, MAX_NAME_LENGTH) else name
    _state.update { state -> state.copy(input = state.input.copy(name = formattedName)) }
  }

  private fun setCover(newCover: String) {
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
    _state.update { it.copy(isSaving = true) }
    createCategoryUseCase(input = input)
      .onSuccess { _effect.emit(CategoryInputScreenEffect.CategoryCreated(it)) }
      .onFailure { _state.update { it.copy(isSaving = false) } }
  }

  private suspend fun updateCategory(
    categoryId: String,
    input: CategoryInput,
  ) {
    _state.update { it.copy(isSaving = true) }
    updateCategoryUseCase(categoryId = categoryId, input = input)
      .onSuccess { category -> _effect.emit(CategoryInputScreenEffect.CategoryUpdated(category)) }
      .onFailure { _state.update { it.copy(isSaving = false) } }
  }

  private suspend fun deleteCategory() {
    categoryId?.let {
      _state.update { it.copy(isDeleting = true) }
      deleteCategoryUseCase(categoryId = categoryId)
        .onSuccess { _effect.emit(CategoryInputScreenEffect.CategoryDeleted(categoryId)) }
        .onFailure { _state.update { it.copy(isDeleting = false) } }
    }
  }

  companion object {
    private const val MAX_NAME_LENGTH = 64

    private val coverRegex = Regex("^.*[а-яА-ЯёЁa-zA-Z0-9!@#\$%ˆ&*()_+-=\"№;%:?*]+?")
  }
}
