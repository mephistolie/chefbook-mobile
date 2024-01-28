package io.chefbook.navigation.navigators

interface BaseNavigator {
  fun navigateUp(skipAnimation: Boolean = false)
}
