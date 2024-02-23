package io.chefbook.features.recipebook.dashboard.ui.navigation

interface DashboardScreenNavigator {
  fun openProfileScreen()
  fun openRecipeInputScreen()

  fun openRecipeBookSearchScreen()
  fun openFavouriteRecipesScreen()
  fun openCategoryRecipesScreen(categoryId: String)
  fun openCommunityRecipesScreen()
  fun openEncryptedVaultScreen()
  fun openShoppingListScreen()
  fun openCategoryInputDialog()
  fun openRecipeScreen(recipeId: String)
}