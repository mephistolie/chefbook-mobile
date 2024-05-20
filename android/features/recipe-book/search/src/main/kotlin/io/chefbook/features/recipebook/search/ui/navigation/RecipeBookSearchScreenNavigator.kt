package io.chefbook.features.recipebook.search.ui.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface RecipeBookSearchScreenNavigator : BaseNavigator {

  fun openRecipeScreen(recipeId: String)

  fun openCategoryRecipesScreen(categoryId: String)

  fun openCommunityRecipeSearch(search: String)
}
