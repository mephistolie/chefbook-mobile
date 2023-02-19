package com.mysty.chefbook.features.category.ui.input

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.category.domain.entities.toInput
import com.mysty.chefbook.api.category.domain.usecases.ICreateCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IDeleteCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IUpdateCategoryUseCase
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.category.ui.input.mvi.CategoryInputDialogEffect
import com.mysty.chefbook.features.category.ui.input.mvi.CategoryInputDialogIntent
import com.mysty.chefbook.features.category.ui.input.mvi.CategoryInputDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias ICategoryInputDialogViewModel = IMviViewModel<CategoryInputDialogState, CategoryInputDialogIntent, CategoryInputDialogEffect>

internal class CategoryInputDialogViewModel(
  private val categoryId: String?,

  private val getCategoryUseCase: IGetCategoryUseCase,
  private val createCategoryUseCase: ICreateCategoryUseCase,
  private val updateCategoryUseCase: IUpdateCategoryUseCase,
  private val deleteCategoryUseCase: IDeleteCategoryUseCase,
) : MviViewModel<CategoryInputDialogState, CategoryInputDialogIntent, CategoryInputDialogEffect>() {

  override val _state: MutableStateFlow<CategoryInputDialogState> =
    MutableStateFlow(CategoryInputDialogState(isEditing = categoryId != null))

  init {
    categoryId?.let {
      viewModelScope.launch {
        getCategoryUseCase(categoryId)
          .collect { result ->
            if (result.isSuccess()) {
              _state.emit(state.value.copy(input = result.data().toInput()))
            }
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

  private suspend fun setName(name: String) {
    withSafeState { state ->
      _state.emit(state.copy(input = state.input.copy(name = name)))
    }
  }

  private suspend fun setCover(newCover: String) {
    val lastCover = state.value.input.cover.orEmpty()
    if (
      (lastCover.length > newCover.length || lastCover.isEmpty()) &&
      (newCover.length <= 10 && !coverRegex.matches(newCover) || newCover.length <= 1)
    ) {
      withSafeState { state ->
        _state.emit(state.copy(input = state.input.copy(cover = newCover.ifEmpty { null })))
      }
    }

  }

  private suspend fun confirmInput() {
    val input = state.value.input
    if (categoryId != null) updateCategory(
      categoryId = categoryId,
      input = input
    ) else createCategory(input = input)
  }

  private suspend fun createCategory(
    input: CategoryInput
  ) {
    createCategoryUseCase(input = input)
      .collect { result ->
        if (result.isSuccess()) _effect.emit(
          CategoryInputDialogEffect.CategoryCreated(
            result.data()
          )
        )
      }
  }

  private suspend fun updateCategory(
    categoryId: String,
    input: CategoryInput
  ) {
    updateCategoryUseCase(categoryId = categoryId, input = input)
      .collect { result ->
        if (result.isSuccess()) _effect.emit(
          CategoryInputDialogEffect.CategoryUpdated(
            result.data()
          )
        )
      }
  }

  private suspend fun deleteCategory() {
    categoryId?.let {
      deleteCategoryUseCase(categoryId = categoryId)
        .collect { result ->
          if (result.isSuccess()) {
            _effect.emit(CategoryInputDialogEffect.CategoryDeleted(categoryId))
          }
        }
    }
  }

  companion object {
    private val coverRegex = Regex("^.*[а-яА-ЯёЁa-zA-Z0-9!@#\$%ˆ&*()_+-=\"№;%:?*]+?")
  }
}
