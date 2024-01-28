package io.chefbook.features.recipebook.favourite.ui.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface RecipeBookFavouriteScreenNavigator : BaseNavigator {
  fun openRecipeScreen(recipeId: String)
}
