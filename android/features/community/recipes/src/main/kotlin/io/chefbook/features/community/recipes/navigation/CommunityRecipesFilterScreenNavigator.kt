package io.chefbook.features.community.recipes.navigation

import io.chefbook.navigation.navigators.BaseNavigator

interface CommunityRecipesFilterScreenNavigator : BaseNavigator {

  fun openTagGroupScreen(groupName: String?)
}
