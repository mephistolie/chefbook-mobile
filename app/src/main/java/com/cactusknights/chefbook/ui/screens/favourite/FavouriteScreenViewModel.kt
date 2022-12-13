package com.cactusknights.chefbook.ui.screens.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.domain.usecases.recipe.IObserveRecipeBookUseCase
import com.cactusknights.chefbook.ui.screens.favourite.models.FavouriteScreenEffect
import com.cactusknights.chefbook.ui.screens.favourite.models.FavouriteScreenEvent
import com.cactusknights.chefbook.ui.screens.favourite.models.FavouriteScreenState
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavouriteScreenViewModel(
    private val observeRecipeBookUseCase: IObserveRecipeBookUseCase,
) : ViewModel(), EventHandler<FavouriteScreenEvent> {

    private val _state: MutableStateFlow<FavouriteScreenState> = MutableStateFlow(FavouriteScreenState())
    val state: StateFlow<FavouriteScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<FavouriteScreenEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<FavouriteScreenEffect> = _effect.asSharedFlow()


    init {
        viewModelScope.launch {
            observeRecipeBookUseCase()
                .onEach { recipes ->
                    _state.emit(FavouriteScreenState(
                        recipes
                            ?.filter { it.isFavourite }
                            ?.sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
                    ))
                }
                .collect()
        }
    }

    override fun obtainEvent(event: FavouriteScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is FavouriteScreenEvent.OpenRecipeScreen -> _effect.emit(FavouriteScreenEffect.OnRecipeOpened(event.recipeId))
                is FavouriteScreenEvent.Back -> _effect.emit(FavouriteScreenEffect.Back)
            }
        }
    }

}
