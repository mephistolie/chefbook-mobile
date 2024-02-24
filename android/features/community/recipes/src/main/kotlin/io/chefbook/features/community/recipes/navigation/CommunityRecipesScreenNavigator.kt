package io.chefbook.features.community.recipes.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import io.chefbook.navigation.navigators.BaseNavigator

interface CommunityRecipesScreenNavigator : BaseNavigator {

  @Composable
  fun currentBackStackEntry(): State<NavBackStackEntry?>

  fun openCommunityLanguagesPickerScreen()

  fun openProfileScreen()

  fun openCommunityRecipesFilterScreen(
    focusSearch: Boolean = false,
    scrollToTags: Boolean = false,
  )

  fun openRecipeScreen(recipeId: String)

  fun openRecipeInputScreen()
}
