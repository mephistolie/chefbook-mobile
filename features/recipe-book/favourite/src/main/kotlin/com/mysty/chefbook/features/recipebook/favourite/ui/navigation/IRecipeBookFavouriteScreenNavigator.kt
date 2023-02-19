package com.mysty.chefbook.features.recipebook.favourite.ui.navigation

import com.mysty.chefbook.navigation.navigators.IBaseNavigator

interface IRecipeBookFavouriteScreenNavigator : IBaseNavigator {
  fun openRecipeScreen(recipeId: String)
}
