package io.chefbook.features.recipebook.categories.ui.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface CategoriesScreenNavigator : BaseNavigator {

  fun openCategoryRecipesScreen(categoryId: String)

  fun openTagRecipesScreen(tagId: String)
}
