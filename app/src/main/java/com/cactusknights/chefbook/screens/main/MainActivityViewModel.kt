package com.cactusknights.chefbook.screens.main

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.BaseViewModel
import com.cactusknights.chefbook.common.Constants.SHOPPING_LIST_BY_DEFAULT
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.domain.usecases.UserUseCases
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val recipeUseCases: RecipesUseCases,
    sp: SharedPreferences
) : BaseViewModel<MainActivityState>(if (!sp.getBoolean(SHOPPING_LIST_BY_DEFAULT, false)) MainActivityState()
else MainActivityState(currentFragment = DashboardFragments.SHOPPING_LIST)
) {

    init {
        viewModelScope.launch {
            observeAuthState()
        }
    }

    private suspend fun observeAuthState() {
        userUseCases.listenToUser().collect { user -> updateUser(user) }
    }

    suspend fun getRecipes() {
        recipeUseCases.getRecipes().onEach { result ->
            when (result) {
                is Result.Loading -> { }
                is Result.Success -> { updateRecipes(result.data!!) }
                is Result.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCases.signOut().collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    else -> {
                        _state.emit(
                            MainActivityState(
                                currentFragment = _state.value.currentFragment,
                                user = null
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun updateUser(user: User?) {
        _state.emit(
            MainActivityState(
                currentFragment = state.value.currentFragment,
                previousFragment = state.value.currentFragment,
                currentCategory = state.value.currentCategory,
                user = user,
                recipes = state.value.recipes,
            )
        )
    }

    private suspend fun updateRecipes(recipes: ArrayList<Recipe>) {
        _state.emit(
            MainActivityState(
                currentFragment = state.value.currentFragment,
                previousFragment = state.value.currentFragment,
                currentCategory = state.value.currentCategory,
                user = state.value.user,
                recipes = recipes,
                )
        )
    }

    fun openFragment(fragment: DashboardFragments, category: Category? = null) {
        viewModelScope.launch {
            _state.emit(
                MainActivityState(
                    currentFragment = fragment,
                    previousFragment = state.value.currentFragment,
                    currentCategory = category,
                    user = state.value.user,
                    recipes = state.value.recipes,
                )
            )
        }
    }
}