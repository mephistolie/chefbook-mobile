package io.chefbook.features.community.recipes.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface CommunityRecipesScreenNavigator : BaseNavigator {

  fun openCommunityLanguagesPickerScreen()

  fun openProfileScreen()

  fun openCommunityRecipesFilterScreen(
    focusSearch: Boolean = false,
    scrollToTags: Boolean = false,
  )

  fun openRecipeScreen(recipeId: String)

  fun openRecipeInputScreen()
}
