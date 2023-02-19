package com.mysty.chefbook.features.recipebook.category.ui.navigation

import com.mysty.chefbook.navigation.navigators.IBaseNavigator

interface ICategoryRecipesScreenNavigator : IBaseNavigator {
  fun openRecipeScreen(recipeId: String)
  fun openCategoryInputDialog(categoryId: String? = null)
}
