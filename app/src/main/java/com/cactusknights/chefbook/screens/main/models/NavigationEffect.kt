package com.cactusknights.chefbook.screens.main.models

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo

sealed class NavigationEffect {
    object StartRecipesFragment : NavigationEffect()
    object StartShoppingListFragment : NavigationEffect()
    object RecipesFragment : NavigationEffect()
    object AddRecipe : NavigationEffect()
    class RecipeOpened(val recipe: RecipeInfo) : NavigationEffect()
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
    class SetTheme(val theme: SettingsProto.AppTheme) : NavigationEffect()
}