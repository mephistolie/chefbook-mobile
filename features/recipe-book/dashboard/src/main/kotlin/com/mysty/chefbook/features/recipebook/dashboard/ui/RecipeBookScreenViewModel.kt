package com.mysty.chefbook.features.recipebook.dashboard.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.category.domain.usecases.IObserveCategoriesUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveLatestRecipesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.recipebook.dashboard.ui.mvi.RecipeBookScreenEffect
import com.mysty.chefbook.features.recipebook.dashboard.ui.mvi.RecipeBookScreenIntent
import com.mysty.chefbook.features.recipebook.dashboard.ui.mvi.RecipeBookScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal typealias IRecipeBookScreenViewModel = IMviViewModel<RecipeBookScreenState, RecipeBookScreenIntent, RecipeBookScreenEffect>

internal class RecipeBookScreenViewModel(
    private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
    private val observeLatestRecipesUseCase: IObserveLatestRecipesUseCase,
    private val observeCategoriesUseCase: IObserveCategoriesUseCase,
    private val observeEncryptionUseCase: IObserveEncryptedVaultStateUseCase,
) : MviViewModel<RecipeBookScreenState, RecipeBookScreenIntent, RecipeBookScreenEffect>() {

    override val _state: MutableStateFlow<RecipeBookScreenState> = MutableStateFlow(RecipeBookScreenState())

    init {
        observeRecipes()
        observeCategories()
        observeEncryption()
    }

    private fun observeRecipes() {
        combine(
            observeRecipeBookUseCase(),
            observeLatestRecipesUseCase()
        ) { recipes, latestRecipesIds ->
            val allRecipes = recipes
                ?.filter { it.encryptionState !is EncryptionState.Encrypted }
                ?.sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
            val latestRecipes =
                latestRecipesIds.mapNotNull { id -> allRecipes?.firstOrNull { it.id == id } }

            _state.emit(state.value.copy(allRecipes = allRecipes, latestRecipes = latestRecipes))
        }
            .launchIn(viewModelScope)
    }

    private fun observeCategories() {
        observeCategoriesUseCase()
            .onEach { categories -> _state.emit(state.value.copy(categories = categories)) }
            .launchIn(viewModelScope)
    }

    private fun observeEncryption() {
        observeEncryptionUseCase()
            .onEach { encryption -> _state.emit(state.value.copy(encryption = encryption)) }
            .launchIn(viewModelScope)
    }

    override suspend fun reduceIntent(intent: RecipeBookScreenIntent) {
        when (intent) {
            is RecipeBookScreenIntent.OpenRecipeSearch -> _effect.emit(RecipeBookScreenEffect.OpenRecipeSearchScreen)
            is RecipeBookScreenIntent.OpenCommunityRecipes -> _effect.emit(RecipeBookScreenEffect.OpenCommunityRecipesScreen)
            is RecipeBookScreenIntent.OpenEncryptionMenu -> _effect.emit(RecipeBookScreenEffect.OpenEncryptedVaultScreen)
            is RecipeBookScreenIntent.OpenCreateRecipeScreen -> _effect.emit(
                RecipeBookScreenEffect.OpenRecipeCreationScreen
            )
            is RecipeBookScreenIntent.OpenCategory -> _effect.emit(
                RecipeBookScreenEffect.OpenCategoryRecipesScreen(
                    intent.categoryId
                )
            )
            is RecipeBookScreenIntent.OpenFavouriteRecipes -> _effect.emit(RecipeBookScreenEffect.OpenFavouriteRecipesScreen)
            is RecipeBookScreenIntent.OpenRecipe -> _effect.emit(
                RecipeBookScreenEffect.OpenRecipeScreen(
                    intent.recipeId
                )
            )
            is RecipeBookScreenIntent.ChangeCategoriesExpanded -> changeCategoriesExpandedState()
            is RecipeBookScreenIntent.ChangeNewCategoryDialogVisibility -> {
                _effect.emit(RecipeBookScreenEffect.OpenCategoryCreationScreen)
            }
        }
    }

    private suspend fun changeCategoriesExpandedState() {
        val lastState = state.value
        _state.emit(lastState.copy(isCategoriesExpanded = !lastState.isCategoriesExpanded))
    }

}
