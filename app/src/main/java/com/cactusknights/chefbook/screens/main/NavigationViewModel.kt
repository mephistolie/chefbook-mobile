package com.cactusknights.chefbook.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.domain.usecases.SettingsUseCases
import com.cactusknights.chefbook.domain.usecases.UserUseCases
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import com.cactusknights.chefbook.screens.main.models.NavigationEffect
import com.cactusknights.chefbook.screens.main.models.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val userUseCases: UserUseCases,
    private val settingsUseCases: SettingsUseCases
) : ViewModel(), EventHandler<NavigationEvent> {

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState.Loading)
    val recipesState: StateFlow<RecipesState> = _recipesState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<NavigationEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<NavigationEffect> = _viewEffect.asSharedFlow()

    private var recipes : List<Recipe> = listOf()

    init {
        viewModelScope.launch {
            launch { userUseCases.getUserInfo() }
            launch { recipeUseCases.listenToRecipes().collect {
                recipes = it
                _recipesState.emit(RecipesState.RecipesUpdated(it))
            } }
        }
    }

    override fun obtainEvent(event: NavigationEvent) {
        viewModelScope.launch {
            when (event) {
                is NavigationEvent.OpenRecipesFragment -> _viewEffect.emit(NavigationEffect.RecipesFragment)
                is NavigationEvent.AddRecipe -> _viewEffect.emit(NavigationEffect.AddRecipe)
                is NavigationEvent.OpenRecipe -> _viewEffect.emit(NavigationEffect.RecipeOpened(event.recipe))
                is NavigationEvent.OpenCategoriesFragment -> _viewEffect.emit(NavigationEffect.CategoriesFragment)
                is NavigationEvent.OpenCategory -> _viewEffect.emit(NavigationEffect.CategoryScreen(event.category))
                is NavigationEvent.OpenFavouriteFragment -> _viewEffect.emit(NavigationEffect.FavouriteFragment)
                is NavigationEvent.OpenShoppingListFragment -> _viewEffect.emit(NavigationEffect.ShoppingListFragment)
                is NavigationEvent.OpenProfile -> _viewEffect.emit(NavigationEffect.ProfileFragment)
                is NavigationEvent.OpenAuthScreen -> _viewEffect.emit(NavigationEffect.SignedOut)
                is NavigationEvent.OpenAboutAppDialog -> _viewEffect.emit(NavigationEffect.AboutAppDialog)
                is NavigationEvent.OpenEditProfileDialog -> _viewEffect.emit(NavigationEffect.EditProfileDialog)
                is NavigationEvent.OpenSettingsDialog -> _viewEffect.emit(NavigationEffect.SettingsFragment)
                is NavigationEvent.RateApp -> _viewEffect.emit(NavigationEffect.RateAppScreen)
                is NavigationEvent.OpenBroccoinsDialog -> _viewEffect.emit(NavigationEffect.BroccoinsDialog)
                is NavigationEvent.OpenSubscriptionDialog -> _viewEffect.emit(NavigationEffect.SubscriptionDialog)
                is NavigationEvent.SearchRecipe -> searchRecipe(event.query)
            }
        }
    }

    private suspend fun searchRecipe(query: String) {
        if (query.isNotEmpty()) _recipesState.emit(RecipesState.SearchResult(recipes.filter { it.name.lowercase().contains(query.lowercase()) }))
        else _recipesState.emit(RecipesState.RecipesUpdated(recipes))
    }
}