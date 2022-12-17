package com.cactusknights.chefbook.ui.screens.recipebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.core.mvi.EventHandler
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.category.domain.usecases.ICreateCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IObserveCategoriesUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveLatestRecipesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenEffect
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenEvent
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RecipeBookScreenViewModel(
    private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
    private val observeLatestRecipesUseCase: IObserveLatestRecipesUseCase,
    private val observeCategoriesUseCase: IObserveCategoriesUseCase,
    private val observeEncryptionUseCase: IObserveEncryptedVaultStateUseCase,
    private val createCategoryUseCase: ICreateCategoryUseCase,
) : ViewModel(), EventHandler<RecipeBookScreenEvent> {

    private val mutex = Mutex()

    private val _state: MutableStateFlow<RecipeBookScreenState> =
        MutableStateFlow(RecipeBookScreenState())
    val state: StateFlow<RecipeBookScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<RecipeBookScreenEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<RecipeBookScreenEffect> = _effect.asSharedFlow()

    init {
        observeRecipes()
        observeCategories()
        observeLatestRecipes()
        observeEncryption()
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            observeRecipeBookUseCase()
                .onEach { recipes ->
                    mutex.withLock {
                        val currentState = state.value
                        val latestRecipesIds = observeLatestRecipesUseCase().first()

                        val allRecipes = recipes
                            ?.filter { it.encryptionState !is EncryptionState.Encrypted }
                            ?.sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
                        val latestRecipes = if (allRecipes != null) {
                            latestRecipesIds.mapNotNull { id -> allRecipes.firstOrNull { it.id == id } }
                        } else {
                            null
                        }
                        _state.emit(currentState.copy(allRecipes = allRecipes, latestRecipes = latestRecipes))
                    }

                }
                .collect()
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            observeCategoriesUseCase().onEach { categories ->
                mutex.withLock {
                    _state.emit(state.value.copy(categories = categories?.sortedBy { it.name }))
                }
            }.collect()
        }
    }

    private fun observeLatestRecipes() {
        viewModelScope.launch {
            observeLatestRecipesUseCase()
                .onEach { latestRecipesIds ->
                    mutex.withLock {
                        val currentState = state.value

                        if (currentState.allRecipes != null) {
                            val latestRecipes = latestRecipesIds.mapNotNull { id -> currentState.allRecipes.firstOrNull { it.id == id } }
                            _state.emit(currentState.copy(latestRecipes = latestRecipes))
                        }
                    }
                }
                .collect()
        }
    }

    private fun observeEncryption() {
        viewModelScope.launch {
            observeEncryptionUseCase()
                .onEach { encryption ->
                    mutex.withLock {
                        _state.emit(state.value.copy(encryption = encryption))
                    }
                }
                .collect()
        }
    }

    private fun handleEncryptedRecipes() {

    }

    override fun obtainEvent(event: RecipeBookScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeBookScreenEvent.OpenRecipeSearch -> _effect.emit(RecipeBookScreenEffect.RecipeSearchOpened)
                is RecipeBookScreenEvent.OpenCommunityRecipes -> _effect.emit(RecipeBookScreenEffect.CommunityRecipesOpened)
                is RecipeBookScreenEvent.OpenEncryptionMenu -> _effect.emit(RecipeBookScreenEffect.EncryptionMenuOpened)
                is RecipeBookScreenEvent.OpenCreateRecipeScreen -> _effect.emit(RecipeBookScreenEffect.RecipeCreationScreenOpened)
                is RecipeBookScreenEvent.CreateCategory -> createCategory(event.input)
                is RecipeBookScreenEvent.OpenCategory -> _effect.emit(RecipeBookScreenEffect.CategoryOpened(event.categoryId))
                is RecipeBookScreenEvent.OpenFavouriteRecipes -> _effect.emit(RecipeBookScreenEffect.FavouriteOpened)
                is RecipeBookScreenEvent.OpenRecipe -> _effect.emit(RecipeBookScreenEffect.RecipeOpened(event.recipeId))
                is RecipeBookScreenEvent.ChangeCategoriesExpanded -> changeCategoriesExpandedState()
                is RecipeBookScreenEvent.ChangeNewCategoryDialogVisibility -> _state.emit(state.value.copy(isNewCategoryDialogVisible = event.isVisible))
            }
        }
    }

    private suspend fun createCategory(input: CategoryInput) {
        createCategoryUseCase(input)
            .onEach { result ->
                if (result.isSuccess()) {
                    _state.emit(state.value.copy(isNewCategoryDialogVisible = false))
                }
            }
            .collect()
    }

    private suspend fun changeCategoriesExpandedState() {
        val lastState = state.value
        _state.emit(lastState.copy(isCategoriesExpanded = !lastState.isCategoriesExpanded))
    }

}
