package com.mysty.chefbook.features.home.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

sealed class HomeScreenIntent : MviIntent {
    object OpenRecipeBook : HomeScreenIntent()
    object OpenOnlineRecipes : HomeScreenIntent()
    object OpenShoppingList : HomeScreenIntent()
    object OpenProfile : HomeScreenIntent()
}
