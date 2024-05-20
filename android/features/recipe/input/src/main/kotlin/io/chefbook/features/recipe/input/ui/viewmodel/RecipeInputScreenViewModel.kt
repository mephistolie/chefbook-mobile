package io.chefbook.features.recipe.input.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenState
import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.GetEncryptedVaultStateUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.ObserveEncryptedVaultStateUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.CreateRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.DeleteRecipeInputPictureUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.GetRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.UpdateRecipeUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.GetDefaultRecipeLanguageUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetDefaultRecipeLanguageUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

private const val BOTTOM_SHEET_HIDE_ANIMATION_DURATION = 200L

@SuppressLint("StaticFieldLeak")
class RecipeInputScreenViewModel(
  private val recipeId: String?,

  private val getRecipeUseCase: GetRecipeUseCase,
  private val createRecipeUseCase: CreateRecipeUseCase,
  private val updateRecipeUseCase: UpdateRecipeUseCase,
  private val observeEncryptedVaultStateUseCase: ObserveEncryptedVaultStateUseCase,
  private val getDefaultRecipeLanguageUseCase: GetDefaultRecipeLanguageUseCase,
  setDefaultRecipeLanguageUseCase: SetDefaultRecipeLanguageUseCase,
  getEncryptedVaultStateUseCase: GetEncryptedVaultStateUseCase,
  private val deleteRecipeInputPictureUseCase: DeleteRecipeInputPictureUseCase,

  private val dispatchers: AppDispatchers,
  private val context: Context,
) : BaseMviViewModel<RecipeInputScreenState, RecipeInputScreenIntent, RecipeInputScreenEffect>() {

  override val _state = MutableStateFlow(
    with(RecipeInputScreenState()) {
      if (recipeId == null) this else copy(isLoading = true, input = input.copy(id = recipeId))
    }
  )

  private var encryptionOnVaultUnlockRequested = false

  private val detailsDelegate = RecipeInputScreenViewModelDetailsDelegate(
    updateState = _state::update,
    sendEffect = _effect::emit,
    requestEncryptionOnVaultUnlock = { encryptionOnVaultUnlockRequested = true },
    setDefaultRecipeLanguageUseCase = setDefaultRecipeLanguageUseCase,
    getEncryptedVaultStateUseCase = getEncryptedVaultStateUseCase,
    deletePicture = ::deletePicture,
  )

  private val ingredientsDelegate = RecipeInputScreenViewModelIngredientsDelegate(
    updateState = _state::update,
    sendEffect = _effect::emit,
  )

  private val cookingDelegate = RecipeInputScreenViewModelCookingDelegate(
    updateState = _state::update,
    sendEffect = _effect::emit,
    deletePicture = ::deletePicture,
  )

  init {
    viewModelScope.launch {
      launch(dispatchers.io) { prepareCacheDir(context) }
      if (recipeId != null) {
        loadRecipe(recipeId = recipeId)
      } else {
        _state.update { state -> state.copy(input = state.input.copy(language = getDefaultRecipeLanguageUseCase())) }
      }
      observeEncryptedVaultUnlock()
    }
  }

  private suspend fun loadRecipe(recipeId: String) {
    _state.emit(state.value.copy(isLoading = true))
    getRecipeUseCase(recipeId)
      .onSuccess { recipe ->
        if (recipe is DecryptedRecipe) {
          _state.update { RecipeInputScreenState(input = recipe.toInput(), isEditing = true) }
        }
      }
      .onFailure { closeInput() }
  }

  private suspend fun observeEncryptedVaultUnlock() {
    observeEncryptedVaultStateUseCase()
      .collect { vaultState ->
        if (vaultState == EncryptedVaultState.UNLOCKED && encryptionOnVaultUnlockRequested) {
          val currentState = state.value
          _state.emit(state.value.copy(input = currentState.input.copy(hasEncryption = true)))
          delay(BOTTOM_SHEET_HIDE_ANIMATION_DURATION)
          _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
          encryptionOnVaultUnlockRequested = false
        }
      }
  }

  override suspend fun reduceIntent(intent: RecipeInputScreenIntent) {
    when (intent) {
      is RecipeInputScreenIntent.Details -> detailsDelegate.reduceIntent(intent.data)
      is RecipeInputScreenIntent.Ingredients -> ingredientsDelegate.reduceIntent(intent.data)
      is RecipeInputScreenIntent.Cooking -> cookingDelegate.reduceIntent(intent.data)

      is RecipeInputScreenIntent.Save -> saveRecipe()
      is RecipeInputScreenIntent.CloseBottomSheet -> _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
      is RecipeInputScreenIntent.Back -> _effect.emit(RecipeInputScreenEffect.OnBack)
      is RecipeInputScreenIntent.Continue -> _effect.emit(RecipeInputScreenEffect.OnContinue)
      is RecipeInputScreenIntent.Close -> closeInput(intent.openRecipeScreen)
    }
  }

  private suspend fun saveRecipe() {
    val currentState = state.value

    _state.emit(currentState.copy(isLoading = true))
    if (recipeId == null) {
      createRecipeUseCase(currentState.input.trim())
        .onSuccess { _effect.emit(RecipeInputScreenEffect.OnSaved) }
    } else {
      updateRecipeUseCase(currentState.input.trim())
        .onSuccess { closeInput(openRecipeScreen = true) }
    }
    _state.emit(currentState)
  }

  private suspend fun closeInput(openRecipeScreen: Boolean = false) {
    _effect.emit(RecipeInputScreenEffect.OnClose(recipeId = if (openRecipeScreen) state.value.input.id else null))
  }

  override fun onCleared() {
    super.onCleared()
    viewModelScope.launch(dispatchers.io + NonCancellable) { deleteCacheDir(context) }
  }

  private fun prepareCacheDir(context: Context) {
    File(getRecipePicturesPath(context, recipeId = state.value.input.id)).mkdirs()
  }

  private fun deletePicture(path: String) {
    viewModelScope.launch { deleteRecipeInputPictureUseCase(path) }
  }

  private fun deleteCacheDir(context: Context) {
    val isEditing = recipeId == null
    if (isEditing) {
      File(getRecipeInputCachePath(context, recipeId = state.value.input.id)).deleteRecursively()
    }
    val cachePath = getRecipeCachePath(context, recipeId = state.value.input.id)
    if (!isEditing || (File(cachePath).list()?.size ?: 0) == 0) {
      File(cachePath).deleteRecursively()
    }
  }
}

fun generatePicturePath(context: Context, recipeId: String): Uri =
  Uri.fromFile(File("${getRecipePicturesPath(context, recipeId)}/${generateUUID()}"))

private fun getRecipePicturesPath(context: Context, recipeId: String) =
  "${getRecipeInputCachePath(context, recipeId)}/pictures"

private fun getRecipeInputCachePath(context: Context, recipeId: String) =
  "${getRecipeCachePath(context, recipeId)}/input"

private fun getRecipeCachePath(context: Context, recipeId: String) =
  "${context.cacheDir.absolutePath}/recipes/$recipeId"