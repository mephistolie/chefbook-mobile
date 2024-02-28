package io.chefbook.features.recipebook.dashboard.ui.navigation

interface DashboardScreenNavigator {

  fun openProfileScreen()

  fun openRecipeBookCreationScreen()

  fun openRecipeBookSearchScreen()

  fun openFavouriteRecipesScreen()

  fun openCategoryRecipesScreen(categoryId: String)

  fun openCommunityRecipesScreen()

  fun openEncryptedVaultScreen()

  fun openShoppingListScreen()

  fun openCategoriesScreen()

  fun openRecipeScreen(recipeId: String)
}