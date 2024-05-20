package io.chefbook.navigation.navigators

interface BaseNavigator {

  fun popBackStackToCurrent() = Unit
  fun navigateUp(skipAnimation: Boolean = false)
}
