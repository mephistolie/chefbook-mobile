package io.chefbook.features.auth.navigation

import io.chefbook.navigation.navigators.DialogNavigator

interface AuthScreenNavigator : DialogNavigator {
  fun openRecipeBookDashboardScreen()
}
