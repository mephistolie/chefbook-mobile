package com.cactusknights.chefbook.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val recipeUseCases: RecipesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state: StateFlow<MainActivityState> = _state

    private var user = User()

    private val _recipes = MutableStateFlow(listOf<BaseRecipe>())
    val recipes: StateFlow<List<BaseRecipe>> = _recipes

    init {
        viewModelScope.launch {
            launch { observeAuthState() }
            getRecipes()
        }
    }

    private suspend fun observeAuthState() {
        authUseCases.authState().collect { auth ->
            if (auth == null) {
                _state.emit(MainActivityState(
                    currentFragment = _state.value.currentFragment,
                    isLoggedIn = false
                ))
            } else user = auth
        }
    }

    private suspend fun getRecipes() {
        recipeUseCases.getRecipes().onEach { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    _recipes.value = result.data!!
                }
                is Result.Error -> {}
            }
        }.launchIn(viewModelScope)
    }
}