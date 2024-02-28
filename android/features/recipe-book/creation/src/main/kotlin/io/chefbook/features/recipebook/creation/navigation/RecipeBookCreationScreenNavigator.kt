package io.chefbook.features.recipebook.creation.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface RecipeBookCreationScreenNavigator : BaseNavigator {

  fun openRecipeInputScreen()

  fun openCategoryInputDialog()
}
