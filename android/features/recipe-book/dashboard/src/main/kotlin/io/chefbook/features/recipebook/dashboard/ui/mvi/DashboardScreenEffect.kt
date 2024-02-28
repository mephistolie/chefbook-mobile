package io.chefbook.features.recipebook.dashboard.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface DashboardScreenEffect : MviSideEffect {
  data object ProfileScreenOpened : DashboardScreenEffect
  data object CreationScreenOpened : DashboardScreenEffect

  data object CommunityRecipesScreenOpened : DashboardScreenEffect
  data object OpenEncryptedVaultScreen : DashboardScreenEffect

  data object ShoppingListScreenOpened : DashboardScreenEffect
  data object RecipeSearchScreenOpened : DashboardScreenEffect
  data object FavouriteRecipesScreenOpened : DashboardScreenEffect

  data class CategoryRecipesScreenOpened(val categoryId: String) : DashboardScreenEffect
  data object CategoriesScreenOpened : DashboardScreenEffect
  data class RecipeScreenOpened(val recipeId: String) : DashboardScreenEffect
}
