package io.chefbook.features.profile.editing.ui.navigation

import io.chefbook.navigation.navigators.DialogNavigator

interface ProfileEditingScreenNavigator : DialogNavigator {
  fun openProfileDeletionScreen()
}
