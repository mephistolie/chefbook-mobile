package io.chefbook.features.recipebook.category.ui.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface CategoryRecipesScreenNavigator : BaseNavigator {
  fun openRecipeScreen(recipeId: String)
  fun openCategoryInputScreen(categoryId: String? = null)
}
