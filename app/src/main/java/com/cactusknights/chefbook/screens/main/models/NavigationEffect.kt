package com.cactusknights.chefbook.screens.main.models

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe

sealed class NavigationEffect {
    object RecipesFragment : NavigationEffect()
    object AddRecipe : NavigationEffect()
    class RecipeOpened(val recipe: Recipe) : NavigationEffect()
    object FavouriteFragment : NavigationEffect()
    object CategoriesFragment : NavigationEffect()
    object ShoppingListFragment : NavigationEffect()
    class CategoryScreen(val category: Category) : NavigationEffect()
    object ProfileFragment : NavigationEffect()
    object AboutAppDialog : NavigationEffect()
    object EditProfileDialog : NavigationEffect()
    object SubscriptionDialog : NavigationEffect()
    object BroccoinsDialog : NavigationEffect()
    object RateAppScreen : NavigationEffect()
    object SettingsFragment : NavigationEffect()
    object SignedOut : NavigationEffect()
}