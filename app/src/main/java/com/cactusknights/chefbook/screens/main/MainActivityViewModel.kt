package com.cactusknights.chefbook.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.enums.SignStates
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.auth.AuthActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val useCases: RecipesUseCases) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state: StateFlow<MainActivityState> = _state

    private val _recipes = MutableStateFlow(listOf<Recipe>())
    val recipes: StateFlow<List<Recipe>> = _recipes

    init {
        getRecipes()
    }

    private fun getRecipes() {
        useCases.getRecipes().onEach { result ->
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