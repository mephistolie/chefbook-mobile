package io.chefbook.features.recipebook.dashboard.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface DashboardScreenIntent : MviIntent {
  data object ProfileButtonClicked : DashboardScreenIntent
  data object CreateButtonClicked : DashboardScreenIntent

  data object OpenCommunityRecipes : DashboardScreenIntent
  data object OpenEncryptionMenu : DashboardScreenIntent

  data object OpenShoppingListScreen : DashboardScreenIntent
  data object OpenRecipeSearch : DashboardScreenIntent
  data object OpenFavouriteRecipes : DashboardScreenIntent

  data class OpenRecipe(val recipeId: String) : DashboardScreenIntent
  data object OpenCategories : DashboardScreenIntent
  data class OpenCategory(val categoryId: String) : DashboardScreenIntent
}
