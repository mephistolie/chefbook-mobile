package io.chefbook.features.recipebook.dashboard.ui.navigation

interface RecipeBookScreenNavigator {
  fun openProfileScreen()
  fun openRecipeInputScreen()

  fun openRecipeBookSearchScreen()
  fun openFavouriteRecipesScreen()
  fun openCategoryRecipesScreen(categoryId: String)
  fun openEncryptedVaultScreen()
  fun openShoppingListScreen()
  fun openCategoryInputDialog()
  fun openRecipeScreen(recipeId: String)
}