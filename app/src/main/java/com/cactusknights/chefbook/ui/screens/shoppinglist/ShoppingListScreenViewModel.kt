package com.cactusknights.chefbook.ui.screens.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.shopinglist.IAddToShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.IObserveShoppingListUseCase
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenEffect
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenEvent
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListState
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation.ShoppingListSectionMapper
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
class ShoppingListScreenViewModel @Inject constructor(
    private val observeShoppingListUseCase: IObserveShoppingListUseCase,
    private val addToShoppingListUseCase: IAddToShoppingListUseCase,
) : ViewModel(), EventHandler<RecipeBookScreenEvent> {

    private val _state: MutableStateFlow<ShoppingListState> = MutableStateFlow(ShoppingListState.Loading)
    val state: StateFlow<ShoppingListState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<RecipeBookScreenEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<RecipeBookScreenEffect> = _effect.asSharedFlow()

    init {
        observeShoppingList()
    }

    private fun observeShoppingList() {
        viewModelScope.launch {
            observeShoppingListUseCase()
                .onEach { shoppingList ->
                    val sections = ShoppingListSectionMapper.map(shoppingList)
                    _state.emit(ShoppingListState.Success(shoppingList = sections))
                }
                .collect()
        }
    }

    override fun obtainEvent(event: RecipeBookScreenEvent) {
        viewModelScope.launch {
//            when (event) {
//                is RecipeBookScreenEvent.OpenRecipeSearch -> _effect.emit(RecipeBookScreenEffect.RecipeSearchOpened)
//                is RecipeBookScreenEvent.OpenCommunityRecipes -> _effect.emit(RecipeBookScreenEffect.CommunityRecipesOpened)
//                is RecipeBookScreenEvent.OpenEncryptionMenu -> _effect.emit(RecipeBookScreenEffect.EncryptionMenuOpened)
//                is RecipeBookScreenEvent.OpenCreateRecipeScreen -> _effect.emit(RecipeBookScreenEffect.RecipeCreationScreenOpened)
//                is RecipeBookScreenEvent.CreateCategory -> createCategory(event.input)
//                is RecipeBookScreenEvent.OpenCategory -> _effect.emit(RecipeBookScreenEffect.CategoryOpened(event.categoryId))
//                is RecipeBookScreenEvent.OpenFavouriteRecipes -> _effect.emit(RecipeBookScreenEffect.FavouriteOpened)
//                is RecipeBookScreenEvent.OpenRecipe -> _effect.emit(RecipeBookScreenEffect.RecipeOpened(event.recipeId))
//                is RecipeBookScreenEvent.ChangeCategoriesExpanded -> changeCategoriesExpandedState()
//                is RecipeBookScreenEvent.ChangeNewCategoryDialogVisibility -> _state.emit(state.value.copy(isNewCategoryDialogVisible = event.isVisible))
//            }
        }
    }

}
