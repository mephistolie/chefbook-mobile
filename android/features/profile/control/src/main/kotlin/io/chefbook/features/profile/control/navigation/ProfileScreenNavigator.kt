package io.chefbook.features.profile.control.navigation

import io.chefbook.navigation.navigators.DialogNavigator

interface ProfileScreenNavigator : DialogNavigator {

  fun openProfileEditingScreen()

  fun openAppSettingsScreen()

  fun openAboutAppScreen()
}