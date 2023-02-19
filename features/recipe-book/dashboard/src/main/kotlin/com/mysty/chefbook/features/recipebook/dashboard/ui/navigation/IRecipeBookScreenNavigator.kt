package com.mysty.chefbook.features.recipebook.dashboard.ui.navigation

interface IRecipeBookScreenNavigator {
  fun openRecipeInputScreen()
  fun openRecipeBookSearchScreen()
  fun openFavouriteRecipesScreen()
  fun openCategoryRecipesScreen(categoryId: String)
  fun openEncryptedVaultScreen()
  fun openCategoryInputDialog()
  fun openRecipeScreen(recipeId: String)
}