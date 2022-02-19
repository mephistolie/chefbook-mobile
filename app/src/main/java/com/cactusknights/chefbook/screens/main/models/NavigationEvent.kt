package com.cactusknights.chefbook.screens.main.models

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo

sealed class NavigationEvent {
    object OpenRecipesFragment : NavigationEvent()
    object AddRecipe : NavigationEvent()
    data class OpenRecipe(val recipe: RecipeInfo) : NavigationEvent()
    object OpenFavouriteFragment : NavigationEvent()
    object OpenCategoriesFragment : NavigationEvent()
    object OpenShoppingListFragment : NavigationEvent()
    data class OpenCategory(val category: Category) : NavigationEvent()
    object OpenProfile : NavigationEvent()
    object OpenAboutAppDialog : NavigationEvent()
    object OpenEditProfileDialog : NavigationEvent()
    object OpenSettingsDialog : NavigationEvent()
    object OpenAuthScreen : NavigationEvent()
    object RateApp : NavigationEvent()
    object OpenBroccoinsDialog : NavigationEvent()
    object OpenSubscriptionDialog : NavigationEvent()
}