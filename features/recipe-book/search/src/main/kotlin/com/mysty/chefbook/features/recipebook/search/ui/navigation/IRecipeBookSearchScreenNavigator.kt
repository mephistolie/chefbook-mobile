package com.mysty.chefbook.features.recipebook.search.ui.navigation

import com.mysty.chefbook.navigation.navigators.IBaseNavigator

interface IRecipeBookSearchScreenNavigator : IBaseNavigator {
  fun openRecipeScreen(recipeId: String)
  fun openCategoryRecipesScreen(categoryId: String)
}