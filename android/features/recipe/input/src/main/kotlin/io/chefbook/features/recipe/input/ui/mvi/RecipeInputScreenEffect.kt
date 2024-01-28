package io.chefbook.features.recipe.input.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

sealed class RecipeInputScreenEffect : MviSideEffect {

  sealed class Details : RecipeInputScreenEffect() {
    data object OnVisibilityPickerOpen : Details()
    data object OnLanguagePickerOpen : Details()
    data object OnEncryptionStatePickerOpen : Details()
    data object OnEncryptedVaultMenuOpen : Details()
    data object OnCaloriesDialogOpen : Details()
  }

  sealed class Ingredients : RecipeInputScreenEffect() {
    data class OnDialogOpen(val ingredientId: String) : RecipeInputScreenEffect()
  }

  data object OnBack : RecipeInputScreenEffect()
  data object OnBottomSheetClosed : RecipeInputScreenEffect()
  data object OnContinue : RecipeInputScreenEffect()
  data object OnSaved : RecipeInputScreenEffect()
  data class OnClose(val recipeId: String?) : RecipeInputScreenEffect()
}
