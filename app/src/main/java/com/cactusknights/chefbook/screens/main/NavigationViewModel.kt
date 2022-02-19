package com.cactusknights.chefbook.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.domain.usecases.SettingsUseCases
import com.cactusknights.chefbook.screens.main.models.NavigationEffect
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    settings: SettingsUseCases
) : ViewModel(), EventHandler<NavigationEvent> {

    private val _viewEffect: MutableSharedFlow<NavigationEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<NavigationEffect> = _viewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            if (settings.getSettings().defaultTab == SettingsProto.Tabs.SHOPPING_LIST) {
                _viewEffect.emit(NavigationEffect.StartShoppingListFragment)
            } else {
                _viewEffect.emit(NavigationEffect.StartRecipesFragment)
            }
            settings.listenToSettings().collect { settings ->
                _viewEffect.emit(NavigationEffect.SetTheme(settings.appTheme))
            }
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
            }
        }
    }
}