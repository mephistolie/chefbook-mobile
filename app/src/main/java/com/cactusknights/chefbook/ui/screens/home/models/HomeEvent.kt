package com.cactusknights.chefbook.ui.screens.home.models

sealed class HomeEvent {
    object OpenRecipeBook : HomeEvent()
    object OpenOnlineRecipes : HomeEvent()
    object OpenShoppingList : HomeEvent()
    object OpenProfile : HomeEvent()
}
