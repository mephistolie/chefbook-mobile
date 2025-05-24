package io.chefbook.features.category.ui.input

import androidx.lifecycle.viewModelScope
import io.chefbook.features.category.ui.input.mvi.CollectionInputScreenEffect
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenIntent
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.api.external.domain.entities.toInput
import io.chefbook.sdk.collection.api.external.domain.usecases.CreateCollectionUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.DeleteCollectionUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.GetCollectionUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.UpdateCollectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CategoryInputScreenViewModel(
  private val collectionId: String?,

  private val getCollectionUseCase: GetCollectionUseCase,
  private val createCollectionUseCase: CreateCollectionUseCase,
  private val updateCollectionUseCase: UpdateCollectionUseCase,
  private val deleteCollectionUseCase: DeleteCollectionUseCase,
) :
  BaseMviViewModel<CategoryInputScreenState, CategoryInputScreenIntent, CollectionInputScreenEffect>() {

  override val _state: MutableStateFlow<CategoryInputScreenState> =
    MutableStateFlow(CategoryInputScreenState(isEditing = collectionId != null))

  init {
    collectionId?.let {
      viewModelScope.launch {
        getCollectionUseCase(collectionId).onSuccess { category ->
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
        if (!isProcessing) _effect.emit(CollectionInputScreenEffect.Cancel)
      }

      is CategoryInputScreenIntent.SetName -> setName(name = intent.name)
      is CategoryInputScreenIntent.SetCover -> setCover(newCover = intent.cover)
      is CategoryInputScreenIntent.ConfirmInput -> confirmInput()
      is CategoryInputScreenIntent.Delete -> _effect.emit(CollectionInputScreenEffect.OpenDeleteConfirmation)
      is CategoryInputScreenIntent.ConfirmDelete -> deleteCategory()
    }
  }

  private fun setName(name: String) {
    val formattedName =
      if (name.length > MAX_NAME_LENGTH) name.substring(0, MAX_NAME_LENGTH) else name
    _state.update { state -> state.copy(input = state.input.copy(name = formattedName)) }
  }

  private fun setCover(newCover: String) {
//    val lastCover = state.value.input.emoji.orEmpty()
//    if (
//      (lastCover.length > newCover.length || lastCover.isEmpty()) &&
//      (newCover.length <= 10 && !coverRegex.matches(newCover) || newCover.length <= 1)
//    ) {
//      _state.update { state -> state.copy(input = state.input.copy(emoji = newCover.ifEmpty { null })) }
//    }

  }

  private suspend fun confirmInput() {
    var input = state.value.input
    input = input.copy(name = input.name.trim())
    if (collectionId != null) updateCategory(
      categoryId = collectionId,
      input = input
    ) else createCategory(input = input)
  }

  private suspend fun createCategory(
    input: CollectionInput
  ) {
    _state.update { it.copy(isSaving = true) }
    createCollectionUseCase(input = input)
//      .onSuccess { _effect.emit(CollectionInputScreenEffect.CollectionCreated(it)) }
      .onFailure { _state.update { it.copy(isSaving = false) } }
  }

  private suspend fun updateCategory(
    categoryId: String,
    input: CollectionInput,
  ) {
    _state.update { it.copy(isSaving = true) }
    updateCollectionUseCase(collectionId = categoryId, input = input)
      .onSuccess { category -> _effect.emit(CollectionInputScreenEffect.CollectionUpdated(category)) }
      .onFailure { _state.update { it.copy(isSaving = false) } }
  }

  private suspend fun deleteCategory() {
    collectionId?.let {
      _state.update { it.copy(isDeleting = true) }
      deleteCollectionUseCase(collectionId = collectionId)
        .onSuccess { _effect.emit(CollectionInputScreenEffect.CollectionDeleted(collectionId)) }
        .onFailure { _state.update { it.copy(isDeleting = false) } }
    }
  }

  companion object {
    private const val MAX_NAME_LENGTH = 64

    private val coverRegex = Regex("^.*[а-яА-ЯёЁa-zA-Z0-9!@#\$%ˆ&*()_+-=\"№;%:?*]+?")
  }
}
