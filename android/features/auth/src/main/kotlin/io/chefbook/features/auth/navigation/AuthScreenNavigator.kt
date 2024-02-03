package io.chefbook.features.auth.form.navigation

import io.chefbook.navigation.navigators.DialogNavigator

interface AuthScreenNavigator : DialogNavigator {
  fun openDashboardScreen()
}
