package com.cactusknights.chefbook.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.Successful
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.usecases.category.IGetCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IDeleteRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IGetRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeFavouriteStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeLikeStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeSaveStatusUseCase
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipePicturesDialogState
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenEffect
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeScreenViewModel @Inject constructor(
    private val getRecipeUseCase: IGetRecipeUseCase,
    private val deleteRecipeUseCase: IDeleteRecipeUseCase,
    private val setRecipeLikeStatusUseCase: ISetRecipeLikeStatusUseCase,
    private val setRecipeSaveStatusUseCase: ISetRecipeSaveStatusUseCase,
    private val setRecipeFavouriteStatusUseCase: ISetRecipeFavouriteStatusUseCase,
    private val setRecipeCategoriesUseCase: ISetRecipeCategoriesUseCase,
    private val getCategoriesUseCase: IGetCategoriesUseCase,
) : ViewModel(), EventHandler<RecipeScreenEvent> {

    private val _state: MutableStateFlow<RecipeScreenState> =
        MutableStateFlow(RecipeScreenState.Loading)
    val state: StateFlow<RecipeScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<RecipeScreenEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<RecipeScreenEffect> = _effect.asSharedFlow()

    override fun obtainEvent(event: RecipeScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeScreenEvent.LoadRecipe -> loadRecipe(event.recipeId)
                is RecipeScreenEvent.ChangeLikeStatus -> changeRecipeLikeStatusUseCase()
                is RecipeScreenEvent.ChangeSavedStatus -> changeRecipeSaveStatusUseCase()
                is RecipeScreenEvent.ChangeFavouriteStatus -> changeRecipeFavouriteStatusUseCase()
                is RecipeScreenEvent.OpenCategoryScreen -> _effect.emit(
                    RecipeScreenEffect.CategoryScreenOpened(
                        event.categoryId
                    )
                )
                is RecipeScreenEvent.ChangeCategories -> openCategoriesSelectionBlock()
                is RecipeScreenEvent.DiscardCategoriesChanging -> closeCategoriesSelectionBlock()
                is RecipeScreenEvent.ConfirmCategoriesChanging -> changeRecipeCategories(event.categories)
                is RecipeScreenEvent.ChangeDialogState -> updateDialog(event)
                is RecipeScreenEvent.EditRecipe -> _effect.emit(RecipeScreenEffect.EditRecipe)
                is RecipeScreenEvent.DeleteRecipe -> deleteRecipe()
            }
        }
    }

    private suspend fun loadRecipe(recipeId: Int) {
        _state.emit(RecipeScreenState.Loading)
        getRecipeUseCase(recipeId)
            .onEach { result ->
                when (result) {
                    is Loading -> _state.emit(RecipeScreenState.Loading)
                    is Successful -> _state.emit(RecipeScreenState.Success(result.data))
                    is Failure -> _state.emit(RecipeScreenState.Error(result.error))
                }
            }
            .collect()
    }

    private suspend fun changeRecipeLikeStatusUseCase() {
        (state.value as? RecipeScreenState.Success)?.let { state ->

            val recipe = state.recipe
            _state.emit(
                state.copy(
                    recipe = recipe.copy(
                        isLiked = !recipe.isLiked,
                        likes = if (recipe.likes != null) {
                            if (recipe.isLiked) recipe.likes - 1 else recipe.likes + 1
                        } else {
                            1
                        }
                    )
                )
            )

            setRecipeLikeStatusUseCase(recipe.id, !recipe.isLiked)
                .onEach { result ->
                    if (result is Failure) _state.emit(state)
                }
                .collect()
        }
    }

    private suspend fun changeRecipeSaveStatusUseCase() {
        (state.value as? RecipeScreenState.Success)?.let { state ->

            val recipe = state.recipe
            _state.emit(
                state.copy(
                    recipe = recipe.copy(
                        isSaved = !recipe.isSaved,
                        isFavourite = false,
                    ),
                    isRemoveFromRecipeBookDialogVisible = false,
                )
            )

            setRecipeSaveStatusUseCase(recipe.id, !recipe.isSaved)
                .onEach { result ->
                    if (result is Failure) _state.emit(state)
                }
                .collect()
        }
    }

    private suspend fun changeRecipeFavouriteStatusUseCase() {
        (state.value as? RecipeScreenState.Success)?.let { state ->

            val recipe = state.recipe
            _state.emit(state.copy(recipe = recipe.copy(isFavourite = !recipe.isFavourite)))

            setRecipeFavouriteStatusUseCase(recipe.id, !recipe.isFavourite)
                .onEach { result ->
                    if (result is Failure) _state.emit(state)
                }
                .collect()
        }
    }

    private suspend fun openCategoriesSelectionBlock() {
        (state.value as? RecipeScreenState.Success)?.let { state ->
            val categories = getCategoriesUseCase.invoke()
            _state.emit(state.copy(categoriesForSelection = categories))
        }
    }

    private suspend fun closeCategoriesSelectionBlock() {
        (state.value as? RecipeScreenState.Success)?.let { state ->
            _state.emit(state.copy(categoriesForSelection = null))
        }
    }

    private suspend fun changeRecipeCategories(newCategoriesIds: List<Int>) {
        (state.value as? RecipeScreenState.Success)?.let { state ->

            val recipe = state.recipe
            val newCategories = state.categoriesForSelection?.filter { it.id in newCategoriesIds }
            _state.emit(
                state.copy(
                    categoriesForSelection = null,
                    recipe = recipe.copy(categories = newCategories ?: emptyList())
                )
            )

            setRecipeCategoriesUseCase.invoke(recipe.id, newCategoriesIds)
                .onEach { result ->
                    if (result is Failure) _state.emit(state)
                }.collect()
        }
    }

    private suspend fun deleteRecipe() {
        (state.value as? RecipeScreenState.Success)?.let { state ->

            deleteRecipeUseCase.invoke(state.recipe.id)
                .onEach { result ->
                    when (result) {
                        is Loading -> _state.emit(RecipeScreenState.Loading)
                        is Successful -> {
                            _effect.emit(RecipeScreenEffect.ScreenClosed(R.string.common_recipe_screen_recipe_deleted))
                        }
                        is Failure -> _effect.emit(RecipeScreenEffect.Toast(R.string.common_general_cant_perform_operation))
                    }
                }.collect()
        }
    }

    private suspend fun updateDialog(event: RecipeScreenEvent.ChangeDialogState) {
        (state.value as? RecipeScreenState.Success)?.let { state ->
            val newState = when (event) {
                is RecipeScreenEvent.ChangeDialogState.Share -> state.copy(isShareDialogVisible = event.isVisible)
                is RecipeScreenEvent.ChangeDialogState.Pictures -> {
                    if (event.isVisible) {
                        val pictures = mutableListOf<String>()
                        if (state.recipe.preview != null) pictures.add(state.recipe.preview)
                        state.recipe.cooking.onEach { item ->
                            if (item is CookingItem.Step) pictures.addAll(item.pictures ?: emptyList())
                        }
                        val startIndex = pictures.indexOfFirst { it == event.selectedPicture }

                        state.copy(
                            picturesDialogState = RecipePicturesDialogState.Visible(
                                pictures = pictures,
                                startIndex = startIndex,
                            )
                        )
                    } else {
                        state.copy(picturesDialogState = RecipePicturesDialogState.Hidden)
                    }
                }
                is RecipeScreenEvent.ChangeDialogState.RemoveFromRecipeBook -> state.copy(isRemoveFromRecipeBookDialogVisible = event.isVisible)
                is RecipeScreenEvent.ChangeDialogState.Delete -> state.copy(isDeleteRecipeDialogVisible = event.isVisible)
            }

            _state.emit(newState)
        }
    }

}
